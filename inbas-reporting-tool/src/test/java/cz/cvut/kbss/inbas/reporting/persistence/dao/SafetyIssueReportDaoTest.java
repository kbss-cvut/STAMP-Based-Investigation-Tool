package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.CorrectiveMeasure;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class SafetyIssueReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private SafetyIssueReportDao reportDao;

    @Autowired
    private SafetyIssueDao safetyIssueDao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsSafetyIssueAsWell() {
        final SafetyIssueReport report = Generator.generateSafetyIssueReport(true, false);
        persistPerson(report.getAuthor());
        reportDao.persist(report);
        assertNotNull(report.getUri());
        assertNotNull(report.getKey());
        assertNotNull(reportDao.find(report.getUri()));
        assertNotNull(safetyIssueDao.find(report.getSafetyIssue().getUri()));
    }

    @Test
    public void updateRemovesOrphanCorrectiveMeasures() {
        final SafetyIssueReport report = persistReportWithMeasures();
        final Set<CorrectiveMeasure> toRemove = new HashSet<>();
        final Iterator<CorrectiveMeasure> it = report.getCorrectiveMeasures().iterator();
        while (it.hasNext()) {
            final CorrectiveMeasure m = it.next();
            if (Generator.randomBoolean()) {
                it.remove();
                toRemove.add(m);
            }
        }
        assertFalse(toRemove.isEmpty());
        reportDao.update(report);

        final SafetyIssueReport result = reportDao.findByKey(report.getKey());
        assertEquals(report.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
        final EntityManager em = emf.createEntityManager();
        try {
            for (CorrectiveMeasure removed : toRemove) {
                assertNull(em.find(CorrectiveMeasure.class, removed.getUri()));
            }
        } finally {
            em.close();
        }
    }

    private SafetyIssueReport persistReportWithMeasures() {
        final SafetyIssueReport report = Generator.generateSafetyIssueReport(true, true);
        persistPerson(report.getAuthor());
        report.getCorrectiveMeasures().stream()
              .filter(m -> m.getResponsibleOrganizations() != null && !m.getResponsibleOrganizations().isEmpty())
              .forEach(m -> m.getResponsibleOrganizations().forEach(this::persistOrganization));
        reportDao.persist(report);
        return report;
    }
}
