package cz.cvut.kbss.reporting.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.environment.config.TestSecurityConfig;
import cz.cvut.kbss.reporting.security.model.LoginStatus;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {TestSecurityConfig.class}, initializers = PropertyMockingApplicationContextInitializer.class)
public class AuthenticationFailureTest extends BaseServiceTestRunner {

    @Autowired
    private AuthenticationFailure failure;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void authenticationFailureReturnsLoginStatusWithErrorInfoOnUsernameNotFound() throws Exception {
        final MockHttpServletRequest request = AuthenticationSuccessTest.request();
        final MockHttpServletResponse response = AuthenticationSuccessTest.response();
        final String msg = "Username not found";
        final AuthenticationException e = new UsernameNotFoundException(msg);
        failure.onAuthenticationFailure(request, response, e);
        final LoginStatus status = mapper.readValue(response.getContentAsString(), LoginStatus.class);
        assertFalse(status.isSuccess());
        assertFalse(status.isLoggedIn());
        assertNull(status.getUsername());
        assertEquals(msg, status.getErrorMessage());
        assertEquals("login.error", status.getErrorId());
    }

    @Test
    public void authenticationFailureReturnsLoginStatusWithErrorInfoOnAccountLocked() throws Exception {
        final MockHttpServletRequest request = AuthenticationSuccessTest.request();
        final MockHttpServletResponse response = AuthenticationSuccessTest.response();
        final String msg = "Account is locked.";
        failure.onAuthenticationFailure(request, response, new LockedException(msg));
        final LoginStatus status = mapper.readValue(response.getContentAsString(), LoginStatus.class);
        assertFalse(status.isSuccess());
        assertFalse(status.isLoggedIn());
        assertNull(status.getUsername());
        assertEquals(msg, status.getErrorMessage());
        assertEquals("login.locked", status.getErrorId());
    }

    @Test
    public void authenticationFailureReturnsLoginStatusWithErrorInfoOnAccountDisabled() throws Exception {
        final MockHttpServletRequest request = AuthenticationSuccessTest.request();
        final MockHttpServletResponse response = AuthenticationSuccessTest.response();
        final String msg = "Account is disabled.";
        failure.onAuthenticationFailure(request, response, new DisabledException(msg));
        final LoginStatus status = mapper.readValue(response.getContentAsString(), LoginStatus.class);
        assertFalse(status.isSuccess());
        assertFalse(status.isLoggedIn());
        assertNull(status.getUsername());
        assertEquals(msg, status.getErrorMessage());
        assertEquals("login.disabled", status.getErrorId());
    }
}