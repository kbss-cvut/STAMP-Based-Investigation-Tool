package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.LogicalDocument;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.util.DocumentDateAndRevisionComparator;
import cz.cvut.kbss.reporting.model.util.EntityToOwlClassMapper;
import cz.cvut.kbss.reporting.persistence.dao.ReportDao;
import cz.cvut.kbss.reporting.service.event.ReportChainRemovalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class MainReportService implements ReportBusinessService, ApplicationEventPublisherAware {

    private final ReportDao reportDao;

    private final OccurrenceReportService occurrenceReportService;

    private final Map<String, Class<? extends LogicalDocument>> entitiesToOwlClasses = new HashMap<>();

    private final Map<Class<? extends LogicalDocument>, BaseReportService<? extends LogicalDocument>> services = new HashMap<>();

    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public MainReportService(ReportDao reportDao, OccurrenceReportService occurrenceReportService) {
        this.reportDao = reportDao;
        this.occurrenceReportService = occurrenceReportService;
    }

    @PostConstruct
    private void initServiceMap() {
        registerService(OccurrenceReport.class, occurrenceReportService);
    }

    private void registerService(Class<? extends LogicalDocument> cls,
                                 BaseReportService<? extends LogicalDocument> service) {
        entitiesToOwlClasses.put(EntityToOwlClassMapper.getOwlClassForEntity(cls), cls);
        services.put(cls, service);
    }

    @Override
    public List<ReportDto> findAll() {
        final List<LogicalDocument> reports = new ArrayList<>();
        services.values().forEach(service -> reports.addAll(service.findAll()));
        return reports.stream().map(LogicalDocument::toReportDto)
                      .sorted(new DocumentDateAndRevisionComparator())
                      .collect(Collectors.toList());
    }

    @Override
    public Page<ReportDto> findAll(Pageable pageSpec, Collection<ReportFilter> filters) {
        final List<LogicalDocument> reports = new ArrayList<>();
        // Combine reports from all services
        services.values().forEach(service -> reports.addAll(service.findAll(pageSpec, filters).getContent()));
        final List<ReportDto> result = reports.stream().map(LogicalDocument::toReportDto)
                                              .sorted(new DocumentDateAndRevisionComparator())
                                              .collect(Collectors.toList());
        // And return corresponding page
        return new PageImpl<>(result.subList(0, Math.min(result.size(), pageSpec.getPageSize())), pageSpec, 0L);
    }

    @Override
    public <T extends LogicalDocument> void persist(T report) {
        Objects.requireNonNull(report);
        resolveService(report).persist(report);
    }

    private <T extends LogicalDocument> BaseReportService<T> resolveService(T instance) {
        if (!services.containsKey(instance.getClass())) {
            throw new UnsupportedReportTypeException("No service found for instance of class " + instance.getClass());
        }
        return (BaseReportService<T>) services.get(instance.getClass());
    }

    private <T extends LogicalDocument> BaseReportService<T> resolveService(Set<String> types) {
        for (String type : types) {
            if (entitiesToOwlClasses.containsKey(type)) {
                return (BaseReportService<T>) services.get(entitiesToOwlClasses.get(type));
            }
        }
        throw new UnsupportedReportTypeException("No service found for instance with types " + types);
    }

    @Override
    public <T extends LogicalDocument> T findByKey(String key) {
        Objects.requireNonNull(key);
        final Set<String> types = reportDao.getReportTypes(key);
        if (types.isEmpty()) {  // No types -> no instance
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findByKey(key);
    }

    @Override
    public <T extends LogicalDocument> void update(T report) {
        Objects.requireNonNull(report);
        resolveService(report).update(report);
    }

    @Override
    public <T extends LogicalDocument> T findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findLatestRevision(fileNumber);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return;
        }
        resolveService(types).removeReportChain(fileNumber);
        notifyOfChainRemoval(fileNumber);
    }

    private void notifyOfChainRemoval(Long fileNumber) {
        eventPublisher.publishEvent(new ReportChainRemovalEvent(this, fileNumber));
    }

    @Override
    public List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return reportDao.getReportChainRevisions(fileNumber);
    }

    @Override
    public <T extends LogicalDocument> T createNewRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        final BaseReportService<T> service = resolveService(types);
        return service.createNewRevision(fileNumber);
    }

    @Override
    public <T extends LogicalDocument> T findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findRevision(fileNumber, revision);
    }

    @Override
    public <T extends LogicalDocument> void transitionToNextPhase(T report) {
        Objects.requireNonNull(report);
        resolveService(report).transitionToNextPhase(report);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
