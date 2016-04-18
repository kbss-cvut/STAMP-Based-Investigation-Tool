package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.model.Report;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.*;

/**
 * Helper DAO for getting various information about reports, without prior knowledge about what kind of report it may
 * be.
 */
@Repository
public class ReportDao {

    @Autowired
    private EntityManagerFactory emf;

    /**
     * Gets latest revisions of all existing report chains.
     *
     * @return List of reports
     */
    public List<Report> findAll() {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE { " +
                    "?x a ?type ; " +
                    "?hasFileNumber ?fileNo ;" +
                    "?hasDateCreated ?dateCreated ;" +
                    "?hasRevision ?revision . " +
                    "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                    "{ ?y a ?type; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . } GROUP BY ?iFileNo }" +
                    "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                    "} ORDER BY DESC(?dateCreated) DESC(?revision)", Report.class)
                     .setParameter("type", URI.create(Vocabulary.Report))
                     .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                     .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                     .setParameter("hasDateCreated", URI.create(Vocabulary.p_dateCreated))
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Gets a set of OWL classes to which report with the specified key belongs.
     *
     * @param key Report identifier (key)
     * @return Report's classes, or an empty set, if there is no such report
     */
    public Set<String> getReportTypes(String key) {
        final EntityManager em = entityManager();
        try {
            final List<String> types = em
                    .createNativeQuery("SELECT ?type WHERE { ?x a ?type ; ?hasKey ?key . }", String.class)
                    .setParameter("hasKey", URI.create(Vocabulary.p_hasKey))
                    .setParameter("key", key, Constants.PU_LANGUAGE)
                    .getResultList();
            return new HashSet<>(types);
        } finally {
            em.close();
        }
    }

    /**
     * Gets a set of OWL Classes to which reports in report chain with the specified identifier belong.
     * <p>
     * The result contains types of all reports in the chain, although it is expected that all the reports will have the
     * same set of types.
     *
     * @param fileNumber Report chain identifier
     * @return Report chain's classes or an empty set, if there is no such chain
     */
    public Set<String> getChainTypes(Long fileNumber) {
        final EntityManager em = entityManager();
        try {
            final List<String> types = em
                    .createNativeQuery("SELECT DISTINCT ?type WHERE { ?x a ?type ; ?hasFileNumber ?fileNo . }",
                            String.class).setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                    .setParameter("fileNo", fileNumber).getResultList();
            return new HashSet<>(types);
        } finally {
            em.close();
        }
    }

    /**
     * Gets a list of revision info instances for a report chain identified by the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return List of revision infos, ordered by revision number (descending)
     */
    public List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final EntityManager em = entityManager();
        try {
            final List rows = em.createNativeQuery(
                    "SELECT ?x ?revision ?key ?created WHERE { ?x a ?type ;" +
                            "?hasRevision ?revision ; " +
                            "?wasCreated ?created ;" +
                            "?hasFileNumber ?fileNo ;" +
                            "?hasKey ?key ." +
                            "} ORDER BY DESC(?revision)")
                                .setParameter("type", URI.create(Vocabulary.Report))
                                .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                                .setParameter("wasCreated", URI.create(Vocabulary.p_dateCreated))
                                .setParameter("hasKey", URI.create(Vocabulary.p_hasKey))
                                .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                                .setParameter("fileNo", fileNumber)
                                .getResultList();
            final List<ReportRevisionInfo> result = new ArrayList<>(rows.size());
            for (Object row : rows) {
                final Object[] rowArr = (Object[]) row;
                final ReportRevisionInfo info = new ReportRevisionInfo();
                info.setUri((URI) rowArr[0]);
                info.setRevision((Integer) rowArr[1]);
                info.setKey((String) rowArr[2]);
                info.setCreated((Date) rowArr[3]);
                result.add(info);
            }
            return result;
        } finally {
            em.close();
        }
    }

    private EntityManager entityManager() {
        return emf.createEntityManager();
    }
}
