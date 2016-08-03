package cz.cvut.kbss.inbas.reporting.persistence.dao.util;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.net.URI;

public class FactorGraphSaver implements FactorGraphNodeVisitor {

    private final EntityManager em;
    private final QuestionSaver questionSaver;

    public FactorGraphSaver(EntityManager em, QuestionSaver questionSaver) {
        this.em = em;
        this.questionSaver = questionSaver;
    }

    @Override
    public void visit(Occurrence occurrence) {
        // Occurrence is persisted separately, just save the question
        if (occurrence.getQuestion() != null) {
            questionSaver.persistIfNecessary(occurrence.getQuestion(), em);
        }
    }

    @Override
    public void visit(Event event) {
        // TODO Fix JOPA: it generates IDs for elements referenced by persisted instances
        if (event.getUri() == null || !exists(event.getUri())) {
            em.persist(event);
            if (event.getQuestion() != null) {
                questionSaver.persistIfNecessary(event.getQuestion(), em);
            }
        }
    }

    private boolean exists(URI uri) {
        return em.createNativeQuery("ASK { ?x a ?type .}", Boolean.class)
                 .setParameter("x", uri)
                 .setParameter("type", URI.create(Vocabulary.s_c_Event)).getSingleResult();
    }
}
