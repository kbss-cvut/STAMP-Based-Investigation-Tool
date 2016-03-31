package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The responsiblePerson/Organization and basedOnEvent/Occurrence fields are here because of the lack of support for
 * inheritance in JOPA. This should be handled on DTO level, where these fields should be replaced with ones using
 * inheritance between agent - Person/Organization and Event - Occurrence.
 */
@OWLClass(iri = Vocabulary.CorrectiveMeasureRequest)
public class CorrectiveMeasureRequest implements HasUri, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasResponsiblePerson, fetch = FetchType.EAGER)
    private Set<Person> responsiblePersons;

    @OWLObjectProperty(iri = Vocabulary.p_hasResponsibleOrganization, fetch = FetchType.EAGER)
    private Set<Organization> responsibleOrganizations;

    @OWLObjectProperty(iri = Vocabulary.p_basedOnEvent, fetch = FetchType.EAGER)
    private Event basedOnEvent;

    @OWLObjectProperty(iri = Vocabulary.p_basedOnOccurrence, fetch = FetchType.EAGER)
    private Occurrence basedOnOccurrence;

    public CorrectiveMeasureRequest() {
    }

    /**
     * Copy constructor.
     * <p>
     * Responsible agents are reused.
     *
     * @param other The instance to copy
     */
    public CorrectiveMeasureRequest(CorrectiveMeasureRequest other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        if (other.responsiblePersons != null) {
            this.responsiblePersons = new HashSet<>(other.responsiblePersons);
        }
        if (other.responsibleOrganizations != null) {
            this.responsibleOrganizations = new HashSet<>(other.responsibleOrganizations);
        }
        this.basedOnEvent = other.basedOnEvent;
        this.basedOnOccurrence = other.basedOnOccurrence;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getBasedOnEvent() {
        return basedOnEvent;
    }

    public void setBasedOnEvent(Event basedOnEvent) {
        this.basedOnEvent = basedOnEvent;
    }

    public Occurrence getBasedOnOccurrence() {
        return basedOnOccurrence;
    }

    public void setBasedOnOccurrence(Occurrence basedOnOccurrence) {
        this.basedOnOccurrence = basedOnOccurrence;
    }

    public Set<Person> getResponsiblePersons() {
        return responsiblePersons;
    }

    public void setResponsiblePersons(Set<Person> responsiblePersons) {
        this.responsiblePersons = responsiblePersons;
    }

    public Set<Organization> getResponsibleOrganizations() {
        return responsibleOrganizations;
    }

    public void setResponsibleOrganizations(
            Set<Organization> responsibleOrganizations) {
        this.responsibleOrganizations = responsibleOrganizations;
    }

    @Override
    public String toString() {
        // First 50 characters of the description
        if (description != null) {
            return "CorrectiveMeasureRequest{" +
                    (description.length() > 50 ? description.substring(0, 50) + "..." : description) + '}';
        }
        return "CorrectiveMeasureRequest{" + uri + "}";
    }
}
