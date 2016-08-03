package cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal;

import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphEdgeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * This traverser uses the traversed items' URIs to determine whether they have been already visited or not.
 */
public class DefaultFactorGraphTraverser extends FactorGraphTraverser {

    private Set<URI> visited;

    public DefaultFactorGraphTraverser(FactorGraphNodeVisitor nodeVisitor,
                                       FactorGraphEdgeVisitor factorGraphEdgeVisitor) {
        super(nodeVisitor, factorGraphEdgeVisitor);
    }

    @Override
    void resetVisited() {
        this.visited = new HashSet<>();
    }

    @Override
    boolean isVisited(FactorGraphItem item) {
        return visited.contains(item.getUri());
    }

    @Override
    void markAsVisited(FactorGraphItem item) {
        visited.add(item.getUri());
    }
}
