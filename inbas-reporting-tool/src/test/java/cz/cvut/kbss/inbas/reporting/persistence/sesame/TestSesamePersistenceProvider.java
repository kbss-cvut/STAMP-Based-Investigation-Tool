package cz.cvut.kbss.inbas.reporting.persistence.sesame;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.openrdf.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSesamePersistenceProvider extends SesamePersistenceProvider {

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public Repository getRepository() {
        final EntityManager em = emf.createEntityManager();
        try {
            return em.unwrap(Repository.class);
        } finally {
            em.close();
        }
    }
}
