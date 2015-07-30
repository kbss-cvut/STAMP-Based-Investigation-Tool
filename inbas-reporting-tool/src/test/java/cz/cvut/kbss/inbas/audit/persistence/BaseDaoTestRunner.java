package cz.cvut.kbss.inbas.audit.persistence;

import cz.cvut.kbss.inbas.audit.test.config.TestPersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ledvima1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
public abstract class BaseDaoTestRunner {
}