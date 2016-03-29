package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.config.ServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.reporting.test.config.TestPersistenceConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, TestPersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseServiceTestRunner {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected Person person;

    @Before
    public void setUp() throws Exception {
        person = Generator.getPerson();
        if (personDao.findByUsername(person.getUsername()) == null) {
            person.encodePassword(passwordEncoder);
            personDao.persist(person);
        }
    }
}
