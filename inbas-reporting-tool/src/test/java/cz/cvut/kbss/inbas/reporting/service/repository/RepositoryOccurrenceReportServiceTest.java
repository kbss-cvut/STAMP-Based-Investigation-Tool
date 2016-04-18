package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RepositoryOccurrenceReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private RepositoryOccurrenceReportService occurrenceReportService;

    private Person author;

    @Before
    public void setUp() {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void persistSetsAuthorDateCreatedFileNumberAndRevision() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        assertNull(report.getAuthor());
        assertNull(report.getDateCreated());
        assertNull(report.getFileNumber());
        assertNull(report.getRevision());
        occurrenceReportService.persist(report);
        verifyPersistedReport(report);
    }

    private void verifyPersistedReport(OccurrenceReport report) {
        assertNotNull(report.getAuthor());
        assertTrue(author.nameEquals(report.getAuthor()));
        assertNotNull(report.getDateCreated());
        assertNotNull(report.getFileNumber());
        assertEquals(Constants.INITIAL_REVISION, report.getRevision());
    }

    @Test
    public void persistCollectionSetsAuthorDateCreatedFileNumberAndRevision() {
        // New file number is used for every instance
        final List<OccurrenceReport> reports = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            reports.add(Generator.generateOccurrenceReport(false));
        }
        occurrenceReportService.persist(reports);
        reports.forEach(this::verifyPersistedReport);
    }

    @Test
    public void createNewRevisionPersistsNewReportWithIncreasedRevisionNumberSameFileNumberCurrentUserAsAuthorCurrentTimeAsCreationDate() {
        final OccurrenceReport firstRevision = persistFirstRevision(false);

        final OccurrenceReport newRevision = occurrenceReportService.createNewRevision(firstRevision.getFileNumber());
        assertNotNull(newRevision);
        assertNotNull(newRevision.getUri());
        assertNotNull(newRevision.getKey());
        assertEquals(firstRevision.getRevision() + 1, newRevision.getRevision().intValue());
        assertEquals(firstRevision.getFileNumber(), newRevision.getFileNumber());
        assertEquals(author, newRevision.getAuthor());
        assertNotEquals(firstRevision.getDateCreated(), newRevision.getDateCreated());
        final OccurrenceReport newRevisionPersisted = occurrenceReportService.find(newRevision.getUri());
        assertNotNull(newRevisionPersisted);
    }

    private OccurrenceReport persistFirstRevision(boolean generateMeasures) {
        final OccurrenceReport firstRevision = Generator.generateOccurrenceReport(true);
        firstRevision.setAuthor(author);
        if (generateMeasures) {
            final Set<CorrectiveMeasureRequest> measures = new HashSet<>();
            for (int i = 0; i < Generator.randomInt(10); i++) {
                final CorrectiveMeasureRequest measureRequest = new CorrectiveMeasureRequest();
                measureRequest.setDescription("Blablabla" + i);
                measureRequest.setBasedOnOccurrence(firstRevision.getOccurrence());
                measureRequest.setResponsiblePersons(Collections.singleton(author));
                measures.add(measureRequest);
            }
            firstRevision.setCorrectiveMeasureRequests(measures);
        }
        occurrenceReportService.persist(firstRevision);
        return firstRevision;
    }

    @Test(expected = NotFoundException.class)
    public void createNewRevisionThrowsNotFoundWhenReportChainDoesNotExist() {
        occurrenceReportService.createNewRevision(Long.MAX_VALUE);
    }

    @Test
    public void createNewRevisionCreatesNewInstancesOfCorrectiveMeasureRequestAndReusesOccurrence() {
        final OccurrenceReport firstRevision = persistFirstRevision(true);
        final Set<URI> measureRequestUris = firstRevision.getCorrectiveMeasureRequests().stream().map(
                CorrectiveMeasureRequest::getUri).collect(Collectors.toSet());

        final OccurrenceReport newRevision = occurrenceReportService.createNewRevision(firstRevision.getFileNumber());
        assertNotNull(newRevision.getCorrectiveMeasureRequests());
        assertEquals(measureRequestUris.size(), newRevision.getCorrectiveMeasureRequests().size());
        newRevision.getCorrectiveMeasureRequests().forEach(mr -> assertFalse(measureRequestUris.contains(mr.getUri())));
        boolean found;
        for (CorrectiveMeasureRequest r : firstRevision.getCorrectiveMeasureRequests()) {
            found = false;
            for (CorrectiveMeasureRequest rr : newRevision.getCorrectiveMeasureRequests()) {
                if (r.getDescription().equals(rr.getDescription())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void createNewRevisionWorksRepeatedly() {
        final OccurrenceReport firstRevision = persistFirstRevision(true);
        final Long fileNumber = firstRevision.getFileNumber();
        final List<OccurrenceReport> chain = new ArrayList<>();
        chain.add(firstRevision);
        for (int i = 0; i < Generator.randomInt(10); i++) {
            chain.add(occurrenceReportService.createNewRevision(fileNumber));
        }

        Integer expectedRevision = Constants.INITIAL_REVISION;
        for (OccurrenceReport r : chain) {
            assertEquals(expectedRevision, r.getRevision());
            assertEquals(fileNumber, r.getFileNumber());
            expectedRevision++;
        }
    }

    @Test
    public void updateSetsLastModifiedAndLastModifiedBy() {
        final OccurrenceReport report = persistFirstRevision(true);
        assertNull(report.getLastModifiedBy());
        assertNull(report.getLastModifiedBy());
        report.setSummary("Report summary.");
        occurrenceReportService.update(report);

        final OccurrenceReport result = occurrenceReportService.find(report.getUri());
        assertEquals(Environment.getCurrentUser().getUri(), result.getLastModifiedBy().getUri());
        assertNotNull(result.getLastModified());
    }
}