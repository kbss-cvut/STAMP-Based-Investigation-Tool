package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.model_new.LogicalDocument;

import java.util.Comparator;

/**
 * Compares two reports by their revision numbers, descending order.
 *
 * @param <T>
 */
public class ReportRevisionComparator<T extends LogicalDocument> implements Comparator<T> {

    @Override
    public int compare(T a, T b) {
        return b.getRevision().compareTo(a.getRevision());
    }
}
