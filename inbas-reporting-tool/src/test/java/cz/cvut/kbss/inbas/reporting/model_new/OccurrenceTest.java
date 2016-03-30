package cz.cvut.kbss.inbas.reporting.model_new;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertTrue;

public class OccurrenceTest {

    @Test
    public void newInstanceHasEventInTypes() {
        final Occurrence o = new Occurrence();
        assertTrue(o.getTypes().contains(Vocabulary.Event));
    }

    /**
     * @see Vocabulary#p_hasEventType
     */
    @Test
    public void setTypeAddsEventTypeUriToInstanceTypesAsWell() {
        final EventType et = new EventType();
        et.setName("2200101 - Runway Incursion by an Aircraft");
        et.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-390/v-2200101"));
        final Occurrence occurrence = new Occurrence();
        occurrence.setType(et);
        assertTrue(occurrence.getTypes().contains(et.getUri().toString()));
    }
}