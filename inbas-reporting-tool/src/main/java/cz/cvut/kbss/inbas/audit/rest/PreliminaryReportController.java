package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReportInfo;
import cz.cvut.kbss.inbas.audit.rest.dto.model.PreliminaryReportDto;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class PreliminaryReportController extends BaseController {

    @Autowired
    private PreliminaryReportService preliminaryReportService;

    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReportInfo> getAllReports() {
        final Collection<PreliminaryReport> reports = preliminaryReportService.findAll();
        return reports.stream().map(reportMapper::occurrenceReportToOccurrenceReportInfo).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreliminaryReportDto getReport(@PathVariable("key") String key) {
        return reportMapper.occurrenceReportToOccurrenceReportDto(getOccurrenceReport(key));
    }

    private PreliminaryReport getOccurrenceReport(String key) {
        final PreliminaryReport original = preliminaryReportService.findByKey(key);
        if (original == null) {
            throw NotFoundException.create("Report", key);
        }
        return original;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@RequestBody PreliminaryReportDto report) {
        final PreliminaryReport preliminaryReport = reportMapper.occurrenceReportDtoToOccurrenceReport(report);
        assert preliminaryReport != null;
        preliminaryReportService.persist(preliminaryReport);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Created report from data {}", report);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody PreliminaryReportDto report) {
        final PreliminaryReport original = getOccurrenceReport(key);
        final PreliminaryReport update = reportMapper.occurrenceReportDtoToOccurrenceReport(report);
        validateReportForUpdate(original, update);
        preliminaryReportService.update(update);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Updated report {}", report);
        }
    }

    private void validateReportForUpdate(PreliminaryReport original, PreliminaryReport update) {
        if (!original.getUri().equals(update.getUri())) {
            throw new ValidationException(
                    "The updated report URI " + update.getUri() + " is different from the original URI " + original
                            .getUri() + "!");
        }
        if (!original.getKey().equals(update.getKey())) {
            throw new ValidationException(
                    "The updated report key " + update.getKey() + " is different from the original key " + original
                            .getKey());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable("key") String key) {
        final PreliminaryReport report = getOccurrenceReport(key);
        preliminaryReportService.remove(report);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Deleted report {}.", report.getUri());
        }
    }
}