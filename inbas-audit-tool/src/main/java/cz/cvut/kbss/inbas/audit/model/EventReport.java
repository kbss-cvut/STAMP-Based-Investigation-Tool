package cz.cvut.kbss.inbas.audit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;
import java.util.Collection;
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
    private Long eventTime;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Person author;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Resource resource;

    @OWLObjectProperty(iri = Vocabulary.p_hasSeverityAssessment)
    private Set<SeverityAssessment> severityAssessments;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure)
    private Set<CorrectiveMeasure> correctiveMeasures;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment)
    private Set<EventTypeAssessment> typeAssessments;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Set<SeverityAssessment> getSeverityAssessments() {
        return severityAssessments;
    }

    public void setSeverityAssessments(Set<SeverityAssessment> severityAssessments) {
        this.severityAssessments = severityAssessments;
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
        final Set<ReportingStatement> statements = new HashSet<>(severityAssessments);
        statements.addAll(correctiveMeasures);
        statements.addAll(typeAssessments);
        return statements;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void generateKey() {
        if (key != null) {
            return;
        }
        this.key = IdentificationUtils.generateKey(uri);
    }

    @Override
    public String toString() {
        return "EventReport{" +
                "uri=" + uri +
                ", eventTime=" + eventTime +
                ", description='" + description + '\'' +
                ", author=" + author +
                '}';
    }
}
