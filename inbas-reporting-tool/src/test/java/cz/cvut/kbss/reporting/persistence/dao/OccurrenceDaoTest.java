package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.TestUtils;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.model.qam.Answer;
import cz.cvut.kbss.reporting.model.qam.Question;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class OccurrenceDaoTest extends BaseDaoTestRunner {

    private static final int MAX_DEPTH = 5;

    @Autowired
    private OccurrenceDao dao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsAllEventsFromFactorGraph() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final Set<Event> events = new HashSet<>();
        generateFactorGraph(occurrence, events, 0);
        dao.persist(occurrence);

        final Occurrence res = dao.find(occurrence.getUri());
        assertNotNull(res);
        assertEquals(occurrence.getKey(), res.getKey());
        final EntityManager em = emf.createEntityManager();
        try {
            for (Event e : events) {
                assertNotNull(em.find(Event.class, e.getUri()));
            }
        } finally {
            em.close();
        }
    }

    private void generateFactorGraph(Occurrence occurrence, Set<Event> events, int depth) {
        for (int i = 0; i < Generator.randomInt(2, 5); i++) {
            final Event evt = event(events);
            occurrence.addChild(evt);
            generateFactorGraph(evt, events, depth + 1);
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Factor f = factor(events);
            occurrence.addFactor(f);
            generateFactorGraph(f.getEvent(), events, depth + 1);
        }
    }

    private Event event(Set<Event> events) {
        final Event evt = new Event();
        evt.setStartTime(new Date());
        evt.setEndTime(new Date());
        evt.setEventTypes(Collections.singleton(Generator.generateEventType()));
        events.add(evt);
        return evt;
    }

    private Factor factor(Set<Event> events) {
        final Factor f = new Factor();
        f.setEvent(event(events));
        f.addType(Generator.randomFactorType());
        return f;
    }

    private void generateFactorGraph(Event event, Set<Event> events, int depth) {
        if (depth == MAX_DEPTH) {
            return;
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Event evt = event(events);
            event.addChild(evt);
            generateFactorGraph(evt, events, depth + 1);
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Factor f = factor(events);
            event.addFactor(f);
            generateFactorGraph(f.getEvent(), events, depth + 1);
        }
    }

    @Test
    public void persistPersistsQuestionsAndAnswersOfEvents() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.setQuestion(Generator.generateQuestions(null));
        dao.persist(occurrence);
        final EntityManager em = emf.createEntityManager();
        try {
            TestUtils.verifyQuestions(occurrence.getQuestion(), question -> {
                final Question res = em.find(Question.class, question.getUri());
                assertNotNull(res);
                assertEquals(question.getTypes().size(), res.getTypes().size());
                assertTrue(question.getTypes().containsAll(res.getTypes()));
                final Set<URI> childUris = question.getSubQuestions().stream().map(Question::getUri)
                                                   .collect(Collectors.toSet());
                assertEquals(question.getSubQuestions().size(), res.getSubQuestions().size());
                res.getSubQuestions().forEach(sq -> assertTrue(childUris.contains(sq.getUri())));
                assertEquals(question.getAnswers().size(), res.getAnswers().size());
                // Assuming only one answer, the string representation can be used for comparison
                assertEquals(question.getAnswers().iterator().next().toString(),
                        res.getAnswers().iterator().next().toString());
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void persistReusesQuestionsWithTheSameUri() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final Set<Event> children = new HashSet<>(2);
        occurrence.setChildren(children);
        event(children);
        final Event evt = children.iterator().next();
        evt.setQuestion(generateReusedQuestions());
        dao.persist(occurrence);
        final EntityManager em = emf.createEntityManager();
        try {
            TestUtils.verifyQuestions(evt.getQuestion(), q -> assertNotNull(em.find(Question.class, q.getUri())));
        } finally {
            em.close();
        }
    }

    private Question generateReusedQuestions() {
        final Question root = Generator.question();
        Question copy = null;
        for (int i = 0; i < 5; i++) {
            copy = Generator.question();
            root.getSubQuestions().add(copy);
        }
        // Copy the first one into the last one to simulate behaviour when multiple question instances (received from the UI)
        // may represent the same one
        final Question first = root.getSubQuestions().iterator().next();
        copy.setUri(first.getUri());
        copy.setTypes(first.getTypes());
        return root;
    }

    @Test
    public void updateRemovesOrphans() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final Set<Event> events = new HashSet<>();
        generateFactorGraph(occurrence, events, 0);
        dao.persist(occurrence);
        final Set<Event> removed = new HashSet<>();
        final Iterator<Event> it = occurrence.getChildren().iterator();
        final Event currentRoot = it.next();
        it.remove();
        removeChildren(currentRoot, removed);
        dao.update(occurrence);

        final EntityManager em = emf.createEntityManager();
        try {
            removed.forEach(e -> assertNull(em.find(Event.class, e.getUri())));
        } finally {
            em.close();
        }
        final Occurrence result = dao.find(occurrence.getUri());
        assertEquals(occurrence.getChildren().size(), result.getChildren().size());
    }

    private void removeChildren(Event event, Set<Event> toRemove) {
        toRemove.add(event);
        if (event.getChildren() != null) {
            event.getChildren().forEach(e -> removeChildren(e, toRemove));
        }
    }

    @Test
    public void removeDeletesAllQuestionsAndAnswersAsWell() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.setQuestion(Generator.generateQuestions(null));
        dao.persist(occurrence);

        dao.remove(occurrence);
        final EntityManager em = emf.createEntityManager();
        try {
            TestUtils.verifyQuestions(occurrence.getQuestion(), question -> {
                assertNull(em.find(Question.class, question.getUri()));
                question.getAnswers().forEach(a -> assertNull(em.find(Answer.class, a.getUri())));
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void persistPersistsAircraftOperatorWhenItDoesNotExist() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.setAircraft(Generator.generateAircraft());
        dao.persist(occurrence);

        final EntityManager em = emf.createEntityManager();
        try {
            final Organization result = em.find(Organization.class, occurrence.getAircraft().getOperator().getUri());
            assertNotNull(result);
        } finally {
            em.close();
        }
    }

    @Test
    public void updatePersistsAircraftOperatorWhenItDoesNotExist() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.setAircraft(Generator.generateAircraft());
        dao.persist(occurrence);

        final Organization oldOrganization = occurrence.getAircraft().getOperator();
        final Organization newOrganization = Generator.generateOrganization();
        occurrence.getAircraft().setOperator(newOrganization);
        dao.update(occurrence);

        final EntityManager em = emf.createEntityManager();
        try {
            assertNotNull(em.find(Organization.class, oldOrganization.getUri()));
            assertNotNull(em.find(Organization.class, newOrganization.getUri()));
        } finally {
            em.close();
        }
    }

    @Test
    public void updateRemovesAircraftIfItWasRemoved() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final Aircraft aircraft = Generator.generateAircraft();
        occurrence.setAircraft(aircraft);
        dao.persist(occurrence);
        final EntityManager em = emf.createEntityManager();
        assertNotNull(dao.find(occurrence.getUri()).getAircraft());

        occurrence.setAircraft(null);
        dao.update(occurrence);

        assertNull(dao.find(occurrence.getUri()).getAircraft());
        try {
            assertNull(em.find(Aircraft.class, aircraft.getUri()));
        } finally {
            em.close();
        }
    }
}