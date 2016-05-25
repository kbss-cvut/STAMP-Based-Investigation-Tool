package cz.cvut.kbss.inbas.reporting.model.util.factorgraph.clone;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Factor;
import cz.cvut.kbss.inbas.reporting.model.FactorType;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphEdgeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;

import java.net.URI;
import java.util.Map;

public class EdgeCloningVisitor implements FactorGraphEdgeVisitor {

    private static final URI HAS_PART_URI = URI.create(Vocabulary.s_p_has_part);

    private final Map<URI, FactorGraphItem> instanceMap;

    public EdgeCloningVisitor(Map<URI, FactorGraphItem> instanceMap) {
        this.instanceMap = instanceMap;
    }

    @Override
    public void visit(URI from, URI to, URI type) {
        final FactorGraphItem source = instanceMap.get(from);
        assert source != null;
        final FactorGraphItem target = instanceMap.get(to);
        assert target != null;
        if (type.equals(HAS_PART_URI)) {
            source.addChild((Event) target);
        } else {
            final Factor factor = new Factor();
            factor.setEvent((Event) source);
            factor.setType(FactorType.fromUri(type));
            target.addFactor(factor);
        }
    }
}
