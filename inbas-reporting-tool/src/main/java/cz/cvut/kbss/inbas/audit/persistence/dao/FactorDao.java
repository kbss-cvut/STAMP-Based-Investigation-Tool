package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FactorDao extends BaseDao<Factor> {

    @Autowired
    private EventTypeAssessmentDao typeAssessmentDao;

    public FactorDao() {
        super(Factor.class);
    }

    @Override
    protected void persist(Factor factor, EntityManager em) {
        if (factor.getAssessment() != null) {
            typeAssessmentDao.persist(factor.getAssessment(), em);
        }
        em.persist(factor);
    }

    @Override
    protected void update(Factor factor, EntityManager em) {
        if (factor.getAssessment() != null) {
            typeAssessmentDao.update(factor.getAssessment(), em);
        }
        em.merge(factor);
    }
}