package cz.cvut.kbss.inbas.reporting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.environment.util.ReportRevisionComparator;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.model_new.Report;
import cz.cvut.kbss.inbas.reporting.model_new.Vocabulary;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.reporting.service.ReportBusinessService;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class ReportControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ReportBusinessService reportServiceMock;

    @Autowired
    private ReportMapper mapper;

    private Person author;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(reportServiceMock);
        this.author = Generator.getPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void getAllReportsReturnsEmptyCollectionWhenThereAreNoReports() throws Exception {
        when(reportServiceMock.findAll()).thenReturn(Collections.emptyList());
        final MvcResult result = mockMvc.perform(get("/reports").accept(MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(status().isOk()).andReturn();
        final List<Report> res = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Report>>() {
                });
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getReportReturnsNotFoundForUnknownKey() throws Exception {
        final String key = "unknownKey";
        when(reportServiceMock.findByKey(key)).thenReturn(null);
        mockMvc.perform(get("/reports/" + key)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetReportForOccurrenceReport() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.getOccurrence().setUri(URI.create(Vocabulary.Occurrence + "#32145"));
        report.setKey(IdentificationUtils.generateKey());
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance12345"));
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final MvcResult result = mockMvc.perform(get("/reports/" + report.getKey())).andExpect(status().isOk())
                                        .andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(report.getUri(), res.getUri());
        assertEquals(report.getKey(), res.getKey());
        assertEquals(report.getOccurrence().getUri(), res.getOccurrence().getUri());
    }

    @Test
    public void testGetLatestRevisionForOccurrenceReport() throws Exception {
        final OccurrenceReport latestRevision = Generator.generateOccurrenceReport(true);
        latestRevision.setRevision(Generator.randomInt(10));
        when(reportServiceMock.findLatestRevision(latestRevision.getFileNumber())).thenReturn(latestRevision);
        final MvcResult result = mockMvc.perform(get("/reports/chain/" + latestRevision.getFileNumber()))
                                        .andExpect(status().isOk()).andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(latestRevision.getUri(), res.getUri());
        assertEquals(latestRevision.getRevision(), res.getRevision());
    }

    @Test
    public void getLatestRevisionThrowsNotFoundWhenReportChainIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        when(reportServiceMock.findLatestRevision(fileNumber)).thenReturn(null);
        mockMvc.perform(get("/reports/chain/" + fileNumber)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetReportChainRevisions() throws Exception {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        Collections.sort(chain, new ReportRevisionComparator<>());  // sort by revision descending
        final Long fileNumber = chain.get(0).getFileNumber();
        final List<ReportRevisionInfo> revisions = new ArrayList<>(chain.size());
        for (int i = 0; i < chain.size(); i++) {
            final OccurrenceReport r = chain.get(i);
            r.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance-" + i));
            r.setKey(IdentificationUtils.generateKey());
            final ReportRevisionInfo revision = new ReportRevisionInfo();
            revision.setUri(r.getUri());
            revision.setRevision(r.getRevision());
            revision.setKey(r.getKey());
            revision.setCreated(r.getDateCreated());
            revisions.add(revision);
        }
        when(reportServiceMock.getReportChainRevisions(fileNumber)).thenReturn(revisions);
        final MvcResult result = mockMvc.perform(get("/reports/chain/" + fileNumber + "/revisions"))
                                        .andExpect(status().isOk()).andReturn();
        final List<ReportRevisionInfo> res = readValue(result, new TypeReference<List<ReportRevisionInfo>>() {
        });
        assertNotNull(res);
        assertEquals(revisions, res);
    }

    @Test
    public void getReportChainRevisionsThrowsNotFoundForUnknownReportChainIdentifier() throws Exception {
        final Long fileNumber = Long.MAX_VALUE;
        when(reportServiceMock.getReportChainRevisions(fileNumber)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/reports/chain/" + fileNumber + "/revisions")).andExpect(status().isNotFound());
        verify(reportServiceMock).getReportChainRevisions(fileNumber);
    }

    @Test
    public void testGetOccurrenceReportRevisionByChainIdentifierAndRevisionNumber() throws Exception {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        chain.forEach(r -> {
            r.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance-" + Generator.randomInt()));
            r.setKey(IdentificationUtils.generateKey());
        });
        final OccurrenceReport report = chain.get(Generator.randomInt(chain.size()) - 1);
        when(reportServiceMock.findRevision(report.getFileNumber(), report.getRevision())).thenReturn(report);
        final MvcResult result = mockMvc
                .perform(get("/reports/chain/" + report.getFileNumber() + "/revisions/" + report.getRevision()))
                .andExpect(status().isOk()).andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(report.getFileNumber(), res.getFileNumber());
        assertEquals(report.getRevision(), res.getRevision());
        assertEquals(report.getUri(), res.getUri());
    }

    @Test
    public void getRevisionThrowsNotFoundWhenRevisionIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        final Integer revision = 3;
        when(reportServiceMock.findRevision(fileNumber, revision)).thenReturn(null);

        mockMvc.perform(get("/reports/chain/" + fileNumber + "/revisions/" + revision))
               .andExpect(status().isNotFound());
    }

    @Test
    public void createReportReturnsLocationOfNewInstance() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        final String key = "117";
        doAnswer(call -> {
            final OccurrenceReport r = (OccurrenceReport) call.getArguments()[0];
            r.setKey(key);
            return null;
        }).when(reportServiceMock).persist(any(OccurrenceReport.class));

        final MvcResult result = mockMvc
                .perform(post("/reports").content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                         .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        verifyLocationEquals("/reports/" + key, result);
        verify(reportServiceMock).persist(any(OccurrenceReport.class));
    }

    private void verifyLocationEquals(String expectedPath, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost" + expectedPath, locationHeader);
    }

    @Test
    public void createReportReturnsValidationExceptionThrownByServiceAsResponse() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        doThrow(new ValidationException("Invalid report.")).when(reportServiceMock)
                                                           .persist(any(OccurrenceReport.class));
        mockMvc.perform(post("/reports").content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                        .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isConflict());
        verify(reportServiceMock).persist(any(OccurrenceReport.class));
    }

    @Test
    public void createNewRevisionReturnsLocationOfNewRevision() throws Exception {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        final Long fileNumber = chain.get(0).getFileNumber();
        Collections.sort(chain, new ReportRevisionComparator<>());  // Sort descending
        final OccurrenceReport newRevision = new OccurrenceReport();
        newRevision.setFileNumber(fileNumber);
        newRevision.setKey(IdentificationUtils.generateKey());
        newRevision.setRevision(chain.get(0).getRevision() + 1);
        when(reportServiceMock.createNewRevision(fileNumber)).thenReturn(newRevision);

        final MvcResult result = mockMvc.perform(post("/reports/chain/" + fileNumber + "/revisions"))
                                        .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals("/reports/" + newRevision.getKey(), result);
    }

    @Test
    public void createNewRevisionReturnsNotFoundThrownByServiceAsResponse() throws Exception {
        final Long fileNumber = Long.MAX_VALUE;
        when(reportServiceMock.createNewRevision(fileNumber))
                .thenThrow(NotFoundException.create("Report chain", fileNumber));
        mockMvc.perform(post("/reports/chain/" + fileNumber + "/revisions")).andExpect(status().isNotFound());
        verify(reportServiceMock).createNewRevision(fileNumber);
    }

    @Test
    public void updateReportPassesReportToServiceForUpdate() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        mockMvc.perform(
                put("/reports/" + report.getKey()).content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                                  .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNoContent());
        final ArgumentCaptor<OccurrenceReport> captor = ArgumentCaptor.forClass(OccurrenceReport.class);
        verify(reportServiceMock).update(captor.capture());
        final OccurrenceReport argument = captor.getValue();
        assertNotNull(argument);
        assertEquals(report.getUri(), argument.getUri());
        assertEquals(report.getKey(), argument.getKey());
    }

    @Test
    public void updateReportThrowsBadRequestWhenKeyInPathDoesNotMatchReportKey() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        final String otherKey = IdentificationUtils.generateKey();
        mockMvc.perform(
                put("/reports/" + otherKey).content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                           .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void updateReportThrowsNotFoundForUnknownReportKey() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(null);
        mockMvc.perform(
                put("/reports/" + report.getKey()).content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                                  .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        verify(reportServiceMock).findByKey(report.getKey());
        verify(reportServiceMock, never()).update(any(OccurrenceReport.class));
    }

    @Test
    public void updateReportReturnsValidationExceptionAsResponseWhenUpdateValidationFails() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        doThrow(new ValidationException("Invalid report.")).when(reportServiceMock).update(any(OccurrenceReport.class));
        mockMvc.perform(
                put("/reports/" + report.getKey()).content(toJson(mapper.occurrenceReportToOccurrenceReportDto(report)))
                                                  .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isConflict());
    }

    @Test
    public void removeChainCallsService() throws Exception {
        final Long fileNumber = IdentificationUtils.generateFileNumber();
        mockMvc.perform(delete("/reports/chain/" + fileNumber)).andExpect(status().isNoContent());
        verify(reportServiceMock).removeReportChain(fileNumber);
    }
}