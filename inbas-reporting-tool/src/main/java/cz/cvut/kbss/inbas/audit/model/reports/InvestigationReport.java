package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.audit.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.InvestigationReport)
public class InvestigationReport implements HasOwlKey, Serializable, ValidatableReport {

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

    //    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_occurrenceCategory, fetch = FetchType.EAGER)
    private EventType occurrenceCategory;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date created;

    @OWLDataProperty(iri = Vocabulary.p_dateLastEdited)
    private Date lastEdited;

    @OWLDataProperty(iri = Vocabulary.p_revision)
    private Integer revision;

    @OWLDataProperty(iri = Vocabulary.p_severityLevel)
    private OccurrenceSeverity severityAssessment;

    // ARMS Attributes BEGIN

    @OWLDataProperty(iri = Vocabulary.p_barrierEffectiveness)
    private BarrierEffectiveness barrierEffectiveness;

    @OWLDataProperty(iri = Vocabulary.p_mostCredibleOutcome)
    private AccidentOutcome accidentOutcome;

    @OWLDataProperty(iri = Vocabulary.p_armsIndex)
    private Short armsIndex;

    // ARMS Attributes END

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

    @OWLObjectProperty(iri = Vocabulary.p_hasFactor, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Factor rootFactor;

    @Types(fetchType = FetchType.EAGER)
    private Set<String> types;

    public InvestigationReport() {
        this.revision = Constants.INITIAL_REVISION;
    }

    public InvestigationReport(PreliminaryReport preliminaryReport) {
        assert preliminaryReport != null;

        this.fileNumber = preliminaryReport.getFileNumber();
        this.occurrence = preliminaryReport.getOccurrence();
        occurrence.transitionToPhase(getPhase());
        this.occurrenceCategory = preliminaryReport.getOccurrenceCategory();
        this.occurrenceStart = preliminaryReport.getOccurrenceStart();
        this.occurrenceEnd = preliminaryReport.getOccurrenceEnd();
        this.barrierEffectiveness = preliminaryReport.getBarrierEffectiveness();
        this.accidentOutcome = preliminaryReport.getAccidentOutcome();
        this.armsIndex = preliminaryReport.getArmsIndex();
        this.summary = preliminaryReport.getSummary();
        this.revision = preliminaryReport.getRevision() + 1;
        this.severityAssessment = preliminaryReport.getSeverityAssessment();
    }

    /**
     * This copy constructors skips root factor, author, last edit author, dates created and last edited.
     */
    public InvestigationReport(InvestigationReport other) {
        assert other != null;

        this.fileNumber = other.fileNumber;
        this.occurrence = other.occurrence;
        this.occurrenceCategory = other.occurrenceCategory;
        this.occurrenceStart = other.occurrenceStart;
        this.occurrenceEnd = other.occurrenceEnd;
        this.severityAssessment = other.severityAssessment;
        this.barrierEffectiveness = other.barrierEffectiveness;
        this.accidentOutcome = other.accidentOutcome;
        this.armsIndex = other.armsIndex;
        this.summary = other.summary;
        if (other.initialReports != null) {
            this.initialReports = other.initialReports.stream().map(InitialReport::new).collect(Collectors.toSet());
        }
        if (other.correctiveMeasures != null) {
            this.correctiveMeasures = other.correctiveMeasures.stream().map(CorrectiveMeasure::new)
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

    public EventType getOccurrenceCategory() {
        return occurrenceCategory;
    }

    public void setOccurrenceCategory(EventType occurrenceCategory) {
        this.occurrenceCategory = occurrenceCategory;
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

    public OccurrenceSeverity getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(OccurrenceSeverity severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public BarrierEffectiveness getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(BarrierEffectiveness barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public AccidentOutcome getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(AccidentOutcome accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public Short getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Short armsIndex) {
        this.armsIndex = armsIndex;
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
        return initialReports;
    }

    public void setInitialReports(Set<InitialReport> initialReports) {
        this.initialReports = initialReports;
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

    /**
     * Root of the factors hierarchy.
     * <p>
     * Root represents the occurrence event itself.
     *
     * @return Root factor
     */
    public Factor getRootFactor() {
        return rootFactor;
    }

    public void setRootFactor(Factor rootFactor) {
        this.rootFactor = rootFactor;
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
        return "InvestigationReport{" +
                "uri=" + uri +
                ", author=" + author +
                ", revision=" + revision +
                ", occurrence=" + occurrence +
                ", summary=" + summary +
                '}';
    }

    @Override
    public ReportingPhase getPhase() {
        return ReportingPhase.INVESTIGATION;
    }
}
