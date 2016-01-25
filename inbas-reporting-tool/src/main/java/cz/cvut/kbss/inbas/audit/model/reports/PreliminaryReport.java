package cz.cvut.kbss.inbas.audit.model.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.PreliminaryReport)
public class PreliminaryReport implements HasOwlKey, Serializable, ValidatableReport {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_fileNumber)
    private Long fileNumber;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_startTime)
    private Date occurrenceStart;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_endTime)
    private Date occurrenceEnd;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date created;

    @OWLDataProperty(iri = Vocabulary.p_dateLastEdited)
    private Date lastEdited;

    @OWLDataProperty(iri = Vocabulary.p_revision)
    private Integer revision;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_severityLevel)
    private OccurrenceSeverity severityAssessment;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String summary;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Person author;

    @OWLObjectProperty(iri = Vocabulary.p_lastEditedBy, fetch = FetchType.EAGER)
    private Person lastEditedBy;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasOccurrence, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @OWLObjectProperty(iri = Vocabulary.p_hasInitialReport, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<InitialReport> initialReports;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<EventTypeAssessment> typeAssessments;

    @Types(fetchType = FetchType.EAGER)
    private Set<String> types;

    public PreliminaryReport() {
        this.revision = Constants.INITIAL_REVISION;
    }

    /**
     * Copy constructor.
     */
    public PreliminaryReport(PreliminaryReport other) {
        this.fileNumber = other.fileNumber;
        this.occurrenceStart = other.occurrenceStart;
        this.occurrenceEnd = other.occurrenceEnd;
        this.severityAssessment = other.severityAssessment;
        this.summary = other.summary;
        this.occurrence = other.occurrence;
        if (other.types != null) {
            new HashSet<>(other.types);
        }
        if (other.initialReports != null) {
            this.initialReports = other.initialReports.stream().map(InitialReport::new).collect(Collectors.toSet());
        }
        if (other.correctiveMeasures != null) {
            this.correctiveMeasures = other.correctiveMeasures.stream().map(CorrectiveMeasure::new)
                                                              .collect(Collectors.toSet());
        }
        if (other.typeAssessments != null) {
            this.typeAssessments = other.typeAssessments.stream().map(EventTypeAssessment::new)
                                                        .collect(Collectors.toSet());
        }
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Date getOccurrenceStart() {
        return occurrenceStart;
    }

    public void setOccurrenceStart(Date occurrenceStart) {
        this.occurrenceStart = occurrenceStart;
    }

    public Date getOccurrenceEnd() {
        return occurrenceEnd;
    }

    public void setOccurrenceEnd(Date occurrenceEnd) {
        this.occurrenceEnd = occurrenceEnd;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Person getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(Person lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Set<InitialReport> getInitialReports() {
        if (initialReports == null) {
            this.initialReports = new HashSet<>();
        }
        return initialReports;
    }

    public void setInitialReports(Set<InitialReport> initialReports) {
        this.initialReports = initialReports;
    }

    public OccurrenceSeverity getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(OccurrenceSeverity severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        if (correctiveMeasures == null) {
            this.correctiveMeasures = new HashSet<>();
        }
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public Set<EventTypeAssessment> getTypeAssessments() {
        return typeAssessments;
    }

    public void setTypeAssessments(Set<EventTypeAssessment> typeAssessments) {
        this.typeAssessments = typeAssessments;
    }

    @JsonIgnore
    public Collection<ReportingStatement> getStatements() {
        final Set<ReportingStatement> statements = new HashSet<>(correctiveMeasures);
        statements.addAll(typeAssessments);
        return statements;
    }

    public Set<String> getTypes() {
        if (types == null) {
            this.types = new HashSet<>(2);
        }
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        Objects.requireNonNull(type);
        getTypes().add(type);
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
    public void generateKey() {
        if (key == null) {
            // Note: this either has to be called before persist or a setter has to be called to make JOPA notice the change
            this.key = IdentificationUtils.generateKey();
        }
    }

    @Override
    public String toString() {
        return "PreliminaryReport{" +
                "uri=" + uri +
                ", author=" + author +
                ", revision=" + revision +
                ", occurrence=" + occurrence +
                ", summary=" + summary +
                '}';
    }

    @Override
    public ReportingPhase getPhase() {
        return ReportingPhase.PRELIMINARY;
    }
}
