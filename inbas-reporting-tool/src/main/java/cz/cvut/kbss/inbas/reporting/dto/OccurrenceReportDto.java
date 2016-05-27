package cz.cvut.kbss.inbas.reporting.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.kbss.inbas.reporting.dto.event.FactorGraph;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.FormGenData;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// It is important that occurrence comes before factorGraph, because it defines a reference to the occurrence, which can the be used
@JsonPropertyOrder(value = {"uri, key, occurrence, factorGraph"})
public class OccurrenceReportDto implements LogicalDocument, FormGenData {

    private URI uri;

    private String key;

    private Long fileNumber;

    private URI phase;

    private OccurrenceDto occurrence;

    private FactorGraph factorGraph;

    private Person author;

    private Date dateCreated;

    private Date lastModified;

    private Person lastModifiedBy;

    private Integer revision;

    private URI severityAssessment;

    private Set<CorrectiveMeasureRequestDto> correctiveMeasures;

    private String summary;

    // ARMS Attributes

    private URI accidentOutcome;

    private URI barrierEffectiveness;

    private Integer armsIndex;

    private Set<String> types;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public OccurrenceDto getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(OccurrenceDto occurrence) {
        this.occurrence = occurrence;
    }

    public FactorGraph getFactorGraph() {
        return factorGraph;
    }

    public void setFactorGraph(FactorGraph factorGraph) {
        this.factorGraph = factorGraph;
    }

    @Override
    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Person getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Person lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Integer getRevision() {
        return revision;
    }

    @Override
    public ReportDto toReportDto() {
        final cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto res = new cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto();
        res.setUri(uri);
        res.setKey(key);
        res.setFileNumber(fileNumber);
        res.setPhase(phase);
        res.setAuthor(author);
        res.setDateCreated(dateCreated);
        res.setLastModifiedBy(lastModifiedBy);
        res.setLastModified(lastModified);
        res.setRevision(revision);
        res.setTypes(types != null ? new HashSet<>(types) : new HashSet<>());
        res.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        res.setIdentification(occurrence.getName());
        res.setDate(occurrence.getStartTime());
        res.setSummary(summary);
        res.setSeverityAssessment(severityAssessment);
        res.setOccurrenceCategory(occurrence.getEventType());
        return res;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasureRequestDto> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequestDto> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public URI getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(URI accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public URI getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(URI barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public Integer getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Integer armsIndex) {
        this.armsIndex = armsIndex;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
