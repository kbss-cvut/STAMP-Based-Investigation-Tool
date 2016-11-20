package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class KeySupportingRepositoryServiceTest extends BaseServiceTestRunner {

    @Autowired
    private RepositoryOccurrenceService occurrenceService;    // This one supports keys

    @Test
    public void testFindByKey() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceService.persist(occurrence);

        final Occurrence result = occurrenceService.findByKey(occurrence.getKey());
        assertNotNull(result);
        assertEquals(occurrence.getUri(), result.getUri());
    }

    @Test
    public void findByKeyCallsPostLoad() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final RepositoryOccurrenceService serviceSpy = spy(occurrenceService);
        serviceSpy.persist(occurrence);

        final Occurrence result = serviceSpy.findByKey(occurrence.getKey());
        assertNotNull(result);
        verify(serviceSpy).postLoad(any(Occurrence.class));
    }
}
