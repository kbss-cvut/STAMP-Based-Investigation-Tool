package cz.cvut.kbss.inbas.reporting.model.util.factorgraph;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Factor;
import cz.cvut.kbss.inbas.reporting.model.util.HasUri;

import java.util.Set;

/**
 * Marker interface for classes used in factor graphs.
 */
public interface FactorGraphItem extends HasUri {

    Set<Event> getChildren();

    void setChildren(Set<Event> children);

    void addChild(Event child);

    Set<Factor> getFactors();

    void setFactors(Set<Factor> factors);

    void addFactor(Factor factor);
}
