package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_factor)
public class Factor implements HasUri, Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor, fetch = FetchType.EAGER)
    private Event event;

    @ParticipationConstraints(nonEmpty = true)
    @Types
    private Set<URI> types;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<URI> getTypes() {
        return types;
    }

    public void setTypes(Set<URI> types) {
        this.types = types;
    }

    public void addType(URI type) {
        Objects.requireNonNull(type);
        if (types == null) {
            this.types = new HashSet<>();
        }
        getTypes().add(type);
    }

    @Override
    public String toString() {
        return event + types.toString();
    }
}
