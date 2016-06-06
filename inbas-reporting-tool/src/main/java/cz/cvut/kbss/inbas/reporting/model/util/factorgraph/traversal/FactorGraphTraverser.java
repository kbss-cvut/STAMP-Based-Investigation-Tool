package cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.util.EventPositionComparator;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphEdgeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Traverses the factor graph, using the specified visitors on corresponding nodes.
 */
public class FactorGraphTraverser {

    private static final URI HAS_PART_URI = URI.create(Vocabulary.s_p_has_part);

    private EventPositionComparator childEventComparator = new EventPositionComparator();

    private FactorGraphNodeVisitor nodeVisitor;
    private FactorGraphEdgeVisitor factorGraphEdgeVisitor;

    public FactorGraphTraverser(FactorGraphNodeVisitor nodeVisitor, FactorGraphEdgeVisitor factorGraphEdgeVisitor) {
        this.nodeVisitor = nodeVisitor;
        this.factorGraphEdgeVisitor = factorGraphEdgeVisitor;
    }

    public void setNodeVisitor(FactorGraphNodeVisitor nodeVisitor) {
        this.nodeVisitor = nodeVisitor;
    }

    public void setFactorGraphEdgeVisitor(FactorGraphEdgeVisitor factorGraphEdgeVisitor) {
        this.factorGraphEdgeVisitor = factorGraphEdgeVisitor;
    }

    /**
     * Traverses factor graph rooted in the specified {@link Occurrence}.
     *
     * @param root Factor graph root
     */
    public void traverse(Occurrence root) {
        final Set<URI> visited = new HashSet<>();
        if (nodeVisitor != null) {
            nodeVisitor.visit(root);
        }
        visited.add(root.getUri());
        if (root.getChildren() != null) {
            root.setChildren(sortChildren(root.getChildren()));
            for (Event e : root.getChildren()) {
                if (factorGraphEdgeVisitor != null) {
                    factorGraphEdgeVisitor.visit(root.getUri(), e.getUri(), HAS_PART_URI);
                }
                traverse(e, visited);
            }
        }
        traverseFactors(root, visited);
    }

    private void traverseFactors(FactorGraphItem item, Set<URI> visited) {
        if (item.getFactors() != null) {
            item.getFactors().forEach(f -> {
                if (factorGraphEdgeVisitor != null) {
                    factorGraphEdgeVisitor.visit(f.getEvent().getUri(), item.getUri(), f.getTypes().iterator().next());
                }
                traverse(f.getEvent(), visited);
            });
        }
    }

    private void traverse(Event event, Set<URI> visited) {
        if (visited.contains(event.getUri())) {
            return;
        }
        if (nodeVisitor != null) {
            nodeVisitor.visit(event);
        }
        visited.add(event.getUri());
        if (event.getChildren() != null) {
            event.setChildren(sortChildren(event.getChildren()));
            event.getChildren().forEach(e -> {
                if (factorGraphEdgeVisitor != null) {
                    factorGraphEdgeVisitor.visit(event.getUri(), e.getUri(), HAS_PART_URI);
                }
                traverse(e, visited);
            });
        }
        traverseFactors(event, visited);
    }

    private Set<Event> sortChildren(Set<Event> children) {
        final Set<Event> sortedChildren = new TreeSet<>(childEventComparator);
        sortedChildren.addAll(children);
        return sortedChildren;
    }
}
