package cz.cvut.kbss.inbas.audit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.EventReport)
public class EventReport implements HasOwlKey {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_date)
    private Date eventTime;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date created;

    @OWLDataProperty(iri = Vocabulary.p_dateLastEdited)
    private Date lastEdited;

    @OWLDataProperty(iri = Vocabulary.p_label)
    private String name;    // Simple name of the event being reported

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLDataProperty(iri = Vocabulary.p_factors)
    private String factors;

    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Person author;

    @OWLObjectProperty(iri = Vocabulary.p_lastEditedBy, fetch = FetchType.EAGER)
    private Person lastEditedBy;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Resource resource;

    @OWLObjectProperty(iri = Vocabulary.p_hasSeverityAssessment, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SeverityAssessment severityAssessment;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<EventTypeAssessment> typeAssessments;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public SeverityAssessment getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(SeverityAssessment severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
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
        statements.add(severityAssessment);
        return statements;
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
        return "EventReport{" +
                "uri=" + uri +
                ", name=" + name +
                ", key=" + key +
                ", author=" + author +
                '}';
    }
}