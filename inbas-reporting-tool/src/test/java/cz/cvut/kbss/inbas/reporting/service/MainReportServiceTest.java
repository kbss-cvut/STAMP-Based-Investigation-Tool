package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.UnsupportedReport;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.service.cache.ReportCache;
import cz.cvut.kbss.inbas.reporting.service.options.ReportingPhaseService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainReportServiceTest extends BaseServiceTestRunner {

    // More tests should be added as additional support for additional report types is added

    @Autowired
    private ReportBusinessService reportService;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private ReportingPhaseService phaseService;

    private Person author;

    @Before
    public void setUp() {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void testPersistOccurrenceReport() {
        final OccurrenceReport report = persistOccurrenceReport();

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertNotNull(result);
    }

    private OccurrenceReport persistOccurrenceReport() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        return report;
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void persistThrowsUnsupportedReportTypeForUnsupportedReportType() {
        final UnsupportedReport report = new UnsupportedReport();
        reportService.persist(report);
    }

    @Test
    public void persistAddsReportIntoCache() {
        assertTrue(reportCache.getAll().isEmpty());
        final OccurrenceReport report = persistOccurrenceReport();

        final List<ReportDto> res = reportCache.getAll();
        assertEquals(1, res.size());
        assertEquals(report.getUri(), res.get(0).getUri());
    }

    @Test
    public void testFindOccurrenceReportByKey() {
        final OccurrenceReport report = persistOccurrenceReport();

        final OccurrenceReport result = reportService.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void findByKeyThrowsUnsupportedReportTypeForReportOfUnknownType() {
        // Occurrences have keys, so it will play a role of unsupported report here
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceService.persist(occurrence);

        reportService.findByKey(occurrence.getKey());
    }

    @Test
    public void findByKeyReturnsNullForUnknownKey() {
        assertNull(reportService.findByKey("unknownKey"));
    }

    @Test
    public void testUpdateOccurrenceReport() {
        final OccurrenceReport report = persistOccurrenceReport();
        final String summary = "Occurrence report summary.";
        report.setSummary(summary);
        reportService.update(report);

        final OccurrenceReport result = occurrenceReportDao.findByKey(report.getKey());
        assertEquals(summary, result.getSummary());
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void updateThrowsUnsupportedReportForUnsupportedReportType() {
        final UnsupportedReport report = new UnsupportedReport();
        reportService.update(report);
    }

    @Test
    public void updateReplacesPreviousInstanceInReportCache() {
        final OccurrenceReport report = persistOccurrenceReport();
        assertEquals(1, reportCache.getAll().size());
        final String summary = "Occurrence report summary.";
        report.setSummary(summary);
        reportService.update(report);

        final OccurrenceReportDto dto = (OccurrenceReportDto) reportCache.getAll().get(0);
        assertEquals(report.getUri(), dto.getUri());
        assertEquals(summary, dto.getSummary());
    }

    @Test
    public void testFindLatestRevisionForOccurrenceReportChain() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final OccurrenceReport latest = chain.get(chain.size() - 1);
        final Long fileNumber = latest.getFileNumber();

        final LogicalDocument result = reportService.findLatestRevision(fileNumber);
        assertNotNull(result);
        assertEquals(latest.getUri(), result.getUri());
        assertEquals(latest.getKey(), result.getKey());
    }

    private List<OccurrenceReport> persistOccurrenceReportChain() {
        final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
        occurrenceReportDao.persist(chain);
        return chain;
    }

    @Test
    public void findLatestRevisionReturnsNullForUnknownReportChain() {
        assertNull(reportService.findLatestRevision(Long.MAX_VALUE));
    }

    @Test
    public void removeReportChainForOccurrenceReportRemovesAllReportsInChain() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        reportService.removeReportChain(fileNumber);

        chain.forEach(r -> assertFalse(occurrenceReportDao.exists(r.getUri())));
    }

    @Test
    public void removeReportChainDoesNothingWhenReportChainDoesNotExist() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        reportService.removeReportChain(Long.MAX_VALUE);

        chain.forEach(r -> assertTrue(occurrenceReportDao.exists(r.getUri())));
    }

    @Test
    public void removeReportChainRemovesInstanceFromReportCache() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        reportCache.put(chain.get(chain.size() - 1).toReportDto());
        assertEquals(1, reportCache.getAll().size());
        final Long fileNumber = chain.get(0).getFileNumber();

        reportService.removeReportChain(fileNumber);
        assertTrue(reportCache.getAll().isEmpty());
    }

    @Test
    public void getChainRevisionsReturnsListOfRevisionInfosForChainOrderedByRevisionDescending() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        // Sort descending by revision number
        Collections.sort(chain, (a, b) -> b.getRevision().compareTo(a.getRevision()));

        final List<ReportRevisionInfo> revisions = reportService.getReportChainRevisions(fileNumber);
        assertEquals(chain.size(), revisions.size());
        for (int i = 0; i < chain.size(); i++) {
            assertEquals(chain.get(i).getUri(), revisions.get(i).getUri());
            assertEquals(chain.get(i).getKey(), revisions.get(i).getKey());
            assertEquals(chain.get(i).getRevision(), revisions.get(i).getRevision());
            assertEquals(chain.get(i).getDateCreated(), revisions.get(i).getCreated());
        }
    }

    @Test
    public void testCreateNewRevisionOfOccurrenceReport() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        final OccurrenceReport latest = chain.get(chain.size() - 1);

        final OccurrenceReport newRevision = reportService.createNewRevision(fileNumber);
        assertNotNull(newRevision);
        assertEquals(latest.getRevision() + 1, newRevision.getRevision().intValue());
        assertEquals(fileNumber, newRevision.getFileNumber());

        final OccurrenceReport result = occurrenceReportDao.find(newRevision.getUri());
        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void createNewRevisionThrowsNotFoundForUnknownReportChainIdentifier() {
        reportService.createNewRevision(Long.MAX_VALUE);
    }

    @Test
    public void createNewRevisionReplacesInstanceInReportCache() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        // Because we are persisting the chain outside MainReportService, the cache does not know about it
        reportCache.put(chain.get(chain.size() - 1).toReportDto());
        assertEquals(1, reportCache.getAll().size());

        final OccurrenceReport newRevision = reportService.createNewRevision(chain.get(0).getFileNumber());
        assertEquals(1, reportCache.getAll().size());
        final OccurrenceReportDto dto = (OccurrenceReportDto) reportCache.getAll().get(0);
        assertEquals(newRevision.getUri(), dto.getUri());
        assertEquals(newRevision.getRevision(), dto.getRevision());
    }

    @Test
    public void testFindRevisionForOccurrenceReport() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final OccurrenceReport report = Environment.randomElement(chain);

        final OccurrenceReport result = reportService.findRevision(report.getFileNumber(), report.getRevision());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void findRevisionReturnsNullForUnknownRevision() {
        assertNull(reportService.findRevision(Long.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void findAllReturnsLatestRevisionsOfAllReportChains() {
        // Once other report types are added, they should be added into this tests
        final List<LogicalDocument> latestRevisions = initReportChains();

        final List<ReportDto> result = reportService.findAll();
        assertTrue(Environment.areEqual(latestRevisions, result));
    }

    @Test
    public void findAllPutsRetrievedReportsIntoCache() {
        // Once other report types are added, they should be added into this tests
        initReportChains();

        assertTrue(reportCache.getAll().isEmpty());
        final List<ReportDto> result = reportService.findAll();
        assertFalse(reportCache.getAll().isEmpty());
        assertEquals(result, reportCache.getAll());
    }

    @Test
    public void findAllRetrievesInstancesFromReportCache() {
        initReportChains();
        // First time will put the reports into cache
        reportService.findAll();
        reportService.findAll();
        // Two calls to service, but only one call to the DAO
        verify(occurrenceReportService).findAll();
    }

    /**
     * Generates report chains.
     *
     * @return List of latest revisions of the generated chains, ordered by date created descending
     */
    private List<LogicalDocument> initReportChains() {
        final List<LogicalDocument> latestRevisions = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final List<OccurrenceReport> chain = persistOccurrenceReportChain();
            latestRevisions.add(chain.get(chain.size() - 1));
        }
        Collections.sort(latestRevisions, (a, b) -> b.getDateCreated().compareTo(a.getDateCreated()));
        return latestRevisions;
    }

    @Test
    public void transitionToNextPhaseTransitionsReportPhase() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setAuthor(author);
        report.setPhase(phaseService.getInitialPhase());
        occurrenceReportService.persist(report);

        final URI expected = phaseService.nextPhase(report.getPhase());
        reportService.transitionToNextPhase(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertEquals(expected, result.getPhase());
    }

    @Test
    public void findAllAfterCacheEvictLoadsReportsFromRepository() throws Exception {
        initReportChains();
        assertTrue(reportCache.getAll().isEmpty());
        assertFalse(reportService.findAll().isEmpty());
        assertFalse(reportCache.getAll().isEmpty());
        reportCache.evict();
        assertTrue(reportCache.getAll().isEmpty());
        assertFalse(reportService.findAll().isEmpty());
        assertFalse(reportCache.getAll().isEmpty());
        verify(occurrenceReportService, times(2)).findAll();
    }
}
