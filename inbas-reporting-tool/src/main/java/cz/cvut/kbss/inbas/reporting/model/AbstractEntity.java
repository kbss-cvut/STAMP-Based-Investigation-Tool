package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;

import java.net.URI;

/**
 * Abstract superclass for all entities with generated identifier.
 */
@MappedSuperclass
public abstract class AbstractEntity implements HasUri {

    @Id(generated = true)
    protected URI uri;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
