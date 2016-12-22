package cz.cvut.kbss.reporting.persistence.dao.formgen;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.TestUtils;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.model.qam.Answer;
import cz.cvut.kbss.reporting.model.qam.Question;
import cz.cvut.kbss.reporting.persistence.PersistenceException;
import cz.cvut.kbss.reporting.persistence.TestEccairsReportImportPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.TestFormGenPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.TestPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.reporting.persistence.dao.PersonDao;
import cz.cvut.kbss.reporting.persistence.sesame.DataDaoPersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {"cz.cvut.kbss.reporting.persistence.dao"})
@ContextConfiguration(classes = {TestPersistenceFactory.class,
        TestFormGenPersistenceFactory.class,
        TestEccairsReportImportPersistenceFactory.class,
        DataDaoPersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OccurrenceReportFormGenDaoTest {

    @Autowired
    private OccurrenceReportFormGenDao dao;

    @Autowired
    private OccurrenceReportDao reportDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    @Qualifier("formGen")
    private EntityManagerFactory emf;

    @Autowired
    private EntityManagerFactory mainEmf;

    @Test
    public void persistSavesNewReportIntoUniqueContext() throws Exception {
        final EntityManager em = emf.createEntityManager();
        RepositoryConnection connection = null;
        try {
            final Repository repository = em.unwrap(Repository.class);
            connection = repository.getConnection();
            assertFalse(connection.getContextIDs().hasNext());
            final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
            report.setFileNumber(null);
            report.setRevision(null);
            report.getAuthor().generateUri();
            final Map<String, URI> ctx = dao.persist(report);
            assertTrue(connection.getContextIDs().hasNext());
            final Set<URI> contexts = getContexts(connection);
            assertTrue(ctx.containsKey(OccurrenceReportFormGenDao.REPORT_CONTEXT_NAME));
            assertTrue(contexts.contains(ctx.get(OccurrenceReportFormGenDao.REPORT_CONTEXT_NAME)));
        } finally {
            em.close();
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Set<URI> getContexts(RepositoryConnection connection) throws Exception {
        final RepositoryResult<Resource> res = connection.getContextIDs();
        final Set<URI> set = new HashSet<>();
        while (res.hasNext()) {
            set.add(URI.create(res.next().stringValue()));
        }
        return set;
    }

    @Test
    public void persistSavesExistingReportIntoUniqueContext() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        personDao.persist(report.getAuthor());
        reportDao.persist(report);
        final Map<String, URI> contexts = dao.persist(report);
        assertFalse(contexts.isEmpty());
    }

    @Test
    public void persistRemovesCorrectiveMeasuresFromReport() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        report.setCorrectiveMeasures(Generator.generateCorrectiveMeasureRequests());
        report.getAuthor().generateUri();
        assertFalse(report.getCorrectiveMeasures().isEmpty());
        dao.persist(report);
        assertTrue(report.getCorrectiveMeasures().isEmpty());
        final EntityManager em = emf.createEntityManager();
        try {
            assertFalse(
                    em.createNativeQuery("ASK { ?x a ?measure . }", Boolean.class).setParameter("measure", URI.create(
                            Vocabulary.s_c_corrective_measure_request)).getSingleResult());
        } finally {
            em.close();
        }
    }

    @Test
    public void testPersistReportWithAuthorAndLastModifiedBySame() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        personDao.persist(report.getAuthor());
        reportDao.persist(report);
        report.setLastModifiedBy(report.getAuthor());
        final Map<String, URI> contexts = dao.persist(report);
        assertFalse(contexts.isEmpty());
    }

    @Test
    public void testPersistReportWithDifferentAuthorAndLastModifier() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        personDao.persist(report.getAuthor());
        final Person lastModifier = new Person();
        lastModifier.setFirstName("Thomas");
        lastModifier.setLastName("Lasky");
        lastModifier.setUsername("lasky@unsc.org");
        personDao.persist(lastModifier);
        report.setLastModifiedBy(lastModifier);
        reportDao.persist(report);

        final Map<String, URI> contexts = dao.persist(report);
        assertFalse(contexts.isEmpty());
    }

    @Test(expected = PersistenceException.class)
    public void throwsPersistenceExceptionWhenInvalidDataIsPersisted() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        report.getOccurrence().setName(null);
        dao.persist(report);
    }

    @Test
    public void persistSavesQuestionAnswerGraph() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        personDao.persist(report.getAuthor());
        final Event evt = report.getOccurrence().getChildren().iterator().next();
        evt.setQuestion(Generator.generateQuestions(null));

        final Map<String, URI> contexts = dao.persist(report);
        assertFalse(contexts.isEmpty());
        final EntityManager em = emf.createEntityManager();
        try {
            final EntityDescriptor descriptor = new EntityDescriptor(
                    contexts.get(OccurrenceReportFormGenDao.REPORT_CONTEXT_NAME));
            TestUtils.verifyQuestions(evt.getQuestion(), q -> {
                assertNotNull(em.find(Question.class, q.getUri(), descriptor));
                if (!q.getAnswers().isEmpty()) {
                    q.getAnswers().forEach(a -> assertNotNull(em.find(Answer.class, a.getUri(), descriptor)));
                }
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void persistExportsE5xDataIntoSeparateContextAndReturnsItsUri() throws Exception {
        final EntityManager em = mainEmf.createEntityManager();
        RepositoryConnection sourceConnection = null;
        try {
            final Repository repository = em.unwrap(Repository.class);
            sourceConnection = repository.getConnection();
            final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
            report.setUri(Generator.generateUri());
            final Collection<Statement> e5Data = persistE5Data(report.getUri(), sourceConnection);
            report.getAuthor().generateUri();
            final Map<String, URI> ctx = dao.persist(report);
            assertTrue(ctx.containsKey(OccurrenceReportFormGenDao.REPORT_CONTEXT_NAME));
            assertTrue(ctx.containsKey(OccurrenceReportFormGenDao.DATA_CONTEXT_NAME));
            verifyStatementPresence(e5Data, ctx.get(OccurrenceReportFormGenDao.DATA_CONTEXT_NAME));
        } finally {
            em.close();
            if (sourceConnection != null) {
                sourceConnection.close();
            }
        }
    }

    private Collection<Statement> persistE5Data(URI contextUri, RepositoryConnection connection) throws Exception {
        final List<Statement> statements = new ArrayList<>();
        final ValueFactory vf = connection.getValueFactory();
        connection.begin();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final org.openrdf.model.URI propertyUri = vf.createURI(Generator.generateUri().toString());
            final Statement stmt = vf
                    .createStatement(vf.createURI(contextUri.toString()), propertyUri, vf.createLiteral(i));
            connection.add(stmt, vf.createURI(contextUri.toString()));
            statements.add(stmt);
        }
        connection.commit();
        return statements;
    }

    private void verifyStatementPresence(Collection<Statement> statements, URI contextUri) throws Exception {
        final EntityManager em = emf.createEntityManager();
        final Repository repo = em.unwrap(Repository.class);
        final RepositoryConnection connection = repo.getConnection();
        try {
            final ValueFactory vf = connection.getValueFactory();
            final org.openrdf.model.URI context = vf.createURI(contextUri.toString());
            for (Statement s : statements) {
                final RepositoryResult rr = connection
                        .getStatements(s.getSubject(), s.getPredicate(), s.getObject(), false, context);
                assertTrue(rr.hasNext());
                rr.close();
            }
        } finally {
            connection.close();
            em.close();
        }
    }
}
