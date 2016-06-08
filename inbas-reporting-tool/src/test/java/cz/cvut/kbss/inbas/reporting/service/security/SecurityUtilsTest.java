package cz.cvut.kbss.inbas.reporting.service.security;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.*;

public class SecurityUtilsTest extends BaseServiceTestRunner {

    @Autowired
    private SecurityUtils securityUtils;

    private Person person;

    @Before
    public void setUp() {
        this.person = Generator.getPerson();
        person.generateUri();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserReturnsCurrentlyLoggedInUser() {
        Environment.setCurrentUser(person);
        final Person result = securityUtils.getCurrentUser();
        assertEquals(person, result);
    }

    @Test
    public void getCurrentUserDetailsReturnsUserDetailsOfCurrentlyLoggedInUser() {
        Environment.setCurrentUser(person);
        final UserDetails result = securityUtils.getCurrentUserDetails();
        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertEquals(person, result.getUser());
    }

    @Test
    public void getCurrentUserDetailsReturnsNullIfNoUserIsLoggedIn() {
        assertNull(securityUtils.getCurrentUserDetails());
    }
}