package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.RunwayIncursion)
public class RunwayIncursion {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_lowVisibilityProcedure)
    private String lowVisibilityProcedure;

    @OWLObjectProperty(iri = Vocabulary.p_hasClearedAircraft, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Aircraft clearedAircraft;

    @OWLObjectProperty(iri = Vocabulary.p_hasIntruder, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Intruder intruder;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLowVisibilityProcedure() {
        return lowVisibilityProcedure;
    }

    public void setLowVisibilityProcedure(String lowVisibilityProcedure) {
        this.lowVisibilityProcedure = lowVisibilityProcedure;
    }

    public Aircraft getClearedAircraft() {
        return clearedAircraft;
    }

    public void setClearedAircraft(Aircraft clearedAircraft) {
        this.clearedAircraft = clearedAircraft;
    }

    public Intruder getIntruder() {
        return intruder;
    }

    public void setIntruder(Intruder intruder) {
        this.intruder = intruder;
    }

    @Override
    public String toString() {
        return "RunwayIncursion{" +
                "clearedAircraft=" + clearedAircraft +
                ", intruder=" + intruder +
                '}';
    }
}