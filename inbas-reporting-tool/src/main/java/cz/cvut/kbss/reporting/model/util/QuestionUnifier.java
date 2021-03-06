package cz.cvut.kbss.reporting.model.util;

import cz.cvut.kbss.reporting.model.AbstractEvent;
import cz.cvut.kbss.reporting.model.qam.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

/**
 * Traverses question-answer tree and unifies multiple occurrences of a question with the same URI.
 * <p>
 * A depth-first search is used for the unification.
 */
public class QuestionUnifier {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionUnifier.class);

    private final Map<URI, Question> visited = new HashMap<>();

    /**
     * Unifies question under the specified event.
     * <p>
     * This means replacing duplicate occurrences of the same question (question with the same URI) with a canonical instance.
     * Where canonical in this case means the first encountered instance.
     *
     * @param event Event whose QA tree will be unified
     */
    public void unifyQuestions(AbstractEvent event) {
        if (event.getQuestion() == null) {
            return;
        }
        unify(event.getQuestion());
    }

    private void unify(Question root) {
        visited.put(root.getUri(), root);
        if (root.getSubQuestions() == null) {
            return;
        }
        final Iterator<Question> it = root.getSubQuestions().iterator();
        final List<Question> replacements = new ArrayList<>(root.getSubQuestions().size());
        while (it.hasNext()) {
            final Question child = it.next();
            if (visited.containsKey(child.getUri())) {
                it.remove();
                if (child.getUri().equals(root.getUri())) {
                    // Not that this won't work for cycles spanning multiple levels of the QA tree
                    LOG.warn("Encountered question-subquestions cycle for question {}.", root);
                    continue;
                }
                replacements.add(visited.get(child.getUri()));
            } else {
                unify(child);
            }
        }
        root.getSubQuestions().addAll(replacements);
    }
}
