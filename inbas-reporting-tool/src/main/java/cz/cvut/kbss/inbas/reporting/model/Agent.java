package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.s_c_Agent)
public class Agent implements HasUri, Serializable {

    @Id
    private URI uri;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
