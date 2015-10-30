package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.exception.UsernameExistsException;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends BaseService<Person> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonDao personDao;

    @Override
    protected GenericDao<Person> getPrimaryDao() {
        return personDao;
    }

    public Person findByUsername(String username) {
        return personDao.findByUsername(username);
    }

    public void persist(Person person) {
        if (findByUsername(person.getUsername()) != null) {
            throw new UsernameExistsException("Username " + person.getUsername() + " already exists.");
        }
        person.generateUri();
        person.encodePassword(passwordEncoder);
        personDao.persist(person);
    }

    @Override
    public void update(Person instance) {
        final Person orig = personDao.find(instance.getUri());
        if (orig == null) {
            throw new IllegalArgumentException("Cannot update person URI");
        }
        if (!orig.getPassword().equals(instance.getPassword())) {
            instance.encodePassword(passwordEncoder);
        }
        personDao.update(instance);
    }

    @Override
    public void remove(Person instance) {
        personDao.remove(instance);
    }
}