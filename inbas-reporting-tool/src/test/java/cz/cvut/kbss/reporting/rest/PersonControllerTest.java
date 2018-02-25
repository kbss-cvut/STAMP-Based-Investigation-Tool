package cz.cvut.kbss.reporting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jsonldjava.utils.JsonUtils;
import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.reporting.dto.PersonUpdateDto;
import cz.cvut.kbss.reporting.environment.config.RestSecurityConfig;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapperImpl;
import cz.cvut.kbss.reporting.rest.handler.ErrorInfo;
import cz.cvut.kbss.reporting.rest.handler.RestExceptionHandler;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.service.PersonService;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestSecurityConfig.class, PersonControllerTest.Config.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebAppConfiguration
public class PersonControllerTest extends BaseControllerTestRunner {

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PersonService personService;

    @Autowired
    private SecurityUtils securityUtilsMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setupObjectMapper();
        // WebApplicationContext is required for proper security. Otherwise, standaloneSetup could be used
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity(springSecurityFilterChain))
                                      .build();
    }

    /**
     * Inner class is necessary to provide the controller as a bean, so that the WebApplicationContext can map it.
     */
    @EnableWebMvc
    @Configuration
    public static class Config extends WebMvcConfigurerAdapter {
        @Mock
        private PersonService personService;

        @Mock
        private SecurityUtils securityUtilsMock;

        @Spy
        private DtoMapper dtoMapper = new DtoMapperImpl();

        @InjectMocks
        private PersonController controller;

        Config() {
            MockitoAnnotations.initMocks(this);
        }

        @Bean
        public PersonService personService() {
            return personService;
        }

        @Bean
        public PersonController personController() {
            return controller;
        }

        @Bean
        public SecurityUtils securityUtils() {
            return securityUtilsMock;
        }

        @Bean
        public RestExceptionHandler restExceptionHandler() {
            return new RestExceptionHandler();
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(Environment.createJsonLdMessageConverter());
            converters.add(Environment.createDefaultMessageConverter());
            converters.add(Environment.createStringEncodingMessageConverter());
            super.configureMessageConverters(converters);
        }
    }

    @Test
    public void getCurrentUserReturnsTheCurrentlyLoggedInUser() throws Exception {
        final Person p = Generator.getPerson();
        p.generateUri();
        Environment.setCurrentUser(p);
        when(personService.findByUsername(p.getUsername())).thenReturn(p);
        MvcResult result = mockMvc.perform(get("/persons/current").principal(Environment.getCurrentUserPrincipal()))
                                  .andReturn();
        final Person res = objectMapper.readValue(result.getResponse().getContentAsString(), Person.class);
        assertEquals(p.getUri(), res.getUri());
        assertTrue(p.nameEquals(res));
    }

    @Test
    public void createPersonPersistsNewPersonAndReturnsLocationHeader() throws Exception {
        authenticateAnonymously();
        final Person p = Generator.getPerson();
        MvcResult result = mockMvc.perform(post("/persons").content(toJson(p)).contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personService).persist(captor.capture());
        assertTrue(p.nameEquals(captor.getValue()));
        verifyLocationEquals("/persons/" + p.getUsername(), result);
    }

    private void authenticateAnonymously() {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
    }

    @Test
    public void createPersonWithoutPasswordReturnsBadRequest() throws Exception {
        authenticateAnonymously();
        final Person p = Generator.getPerson();
        p.setPassword(null);
        final String err = "Missing password.";
        doThrow(new ValidationException(err)).when(personService).persist(any(Person.class));
        MvcResult result = mockMvc.perform(post("/persons").content(toJson(p)).contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();
        assertEquals(HttpStatus.CONFLICT, HttpStatus.valueOf(result.getResponse().getStatus()));
        final ErrorInfo errorInfo = readValue(result, ErrorInfo.class);
        assertNotNull(errorInfo);
        assertEquals(err, errorInfo.getMessage());
    }

    @Test
    public void updateUserUpdatedUserData() throws Exception {
        final Person person = Generator.getPerson();
        person.generateUri();
        Environment.setCurrentUser(person);
        final Person update = new Person();
        update.setUri(person.getUri());
        update.setFirstName("UpdatedFirstName");
        update.setLastName("UpdatedLastName");
        update.setUsername(person.getUsername());
        mockMvc.perform(put("/persons/current").content(toJson(update)).contentType(MediaType.APPLICATION_JSON)
                                               .principal(Environment.getCurrentUserPrincipal()))
               .andExpect(status().isNoContent());
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personService).update(captor.capture());
        final Person argument = captor.getValue();
        assertTrue(update.nameEquals(argument));
        verify(securityUtilsMock, never()).verifyCurrentUserPassword(any());
    }

    @Test
    public void updateUserVerifiesOldPasswordWhenNewOneIsSpecified() throws Exception {
        final Person person = Generator.getPerson();
        person.generateUri();
        Environment.setCurrentUser(person);
        final TestPersonDto update = new TestPersonDto();
        update.setUri(person.getUri());
        update.setFirstName(person.getFirstName());
        update.setLastName(person.getLastName());
        update.setUsername(person.getUsername());
        update.setPassword("newPassword");
        update.setPasswordOriginal(person.getPassword());
        when(securityUtilsMock.getCurrentUser()).thenReturn(person);
        mockMvc.perform(put("/persons/current").content(toJson(update)).contentType(MediaType.APPLICATION_JSON)
                                               .principal(Environment.getCurrentUserPrincipal()))
               .andExpect(status().isNoContent());
        verify(securityUtilsMock).verifyCurrentUserPassword(update.getPasswordOriginal());
    }

    private static class TestPersonDto extends PersonUpdateDto {
        // We are using this to bypass the WRITE_ONLY access to the password property in Person
        private String password;

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Test
    public void doesUsernameExistReturnsValueForQuery() throws Exception {
        Environment.setCurrentUser(Generator.getPerson());
        when(personService.exists(anyString())).thenReturn(true);
        final String username = "masterchief";
        final MvcResult mvcResult = mockMvc
                .perform(get("/persons/exists").param("username", username).accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk()).andReturn();
        final String result = mvcResult.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(result));
        verify(personService).exists(username);
    }

    @Test
    public void findAllReturnsAllUsers() throws Exception {
        final Person user = Generator.getPerson();
        user.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(user);
        final List<Person> persons = generatePersons();
        when(personService.findAll()).thenReturn(persons);

        final MvcResult mvcResult = mockMvc.perform(get("/persons")).andExpect(status().isOk()).andReturn();
        final List<Person> result = readValue(mvcResult, new TypeReference<List<Person>>() {
        });
        assertEquals(persons.size(), result.size());
        for (int i = 0; i < persons.size(); i++) {
            assertEquals(persons.get(i).getUri(), result.get(i).getUri());
            assertNull(result.get(i).getPassword());
        }
    }

    private List<Person> generatePersons() {
        return IntStream.range(0, 5).mapToObj(i -> {
            final Person p = new Person();
            p.setUri(Generator.generateUri());
            p.setFirstName("firstName" + i);
            p.setLastName("lastName" + i);
            p.setUsername("username" + i);
            p.setPassword("password" + i);
            return p;
        }).collect(Collectors.toList());
    }

    @Test
    public void findAllThrowsForbiddenForUnauthorizedUser() throws Exception {
        Environment.setCurrentUser(Generator.getPerson());
        when(personService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/persons")).andExpect(status().isForbidden());
    }

    @Test
    public void unlockUserInvokesUnlockOnPersonService() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final Person user = new Person();
        user.setUsername("locked@inbas.cz");
        user.lock();
        final String newPassword = "newPassword";
        when(personService.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(put("/persons/unlock").param("username", user.getUsername()).content(newPassword))
               .andExpect(status().isNoContent());
        verify(personService).findByUsername(user.getUsername());
        verify(personService).unlock(user, newPassword);
    }

    @Test
    public void unlockThrowsNotFoundForUnknownUsername() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final String username = "unknownOne";
        when(personService.findByUsername(username)).thenReturn(null);

        mockMvc.perform(put("/persons/unlock").param("username", username).content("newPassword"))
               .andExpect(status().isNotFound());
        verify(personService).findByUsername(username);
        verify(personService, never()).unlock(any(), anyString());
    }

    @Test
    public void unlockUserThrowsForbiddenForNonAdmin() throws Exception {
        final Person nonAdmin = Generator.getPerson();
        Environment.setCurrentUser(nonAdmin);
        final String username = "locked@inbas.cz";
        final String newPassword = "newPassword";
        final UserDetails userDetails = new UserDetails(nonAdmin);

        mockMvc.perform(put("/persons/unlock").param("username", username).content(newPassword).with(user(userDetails)))
               .andExpect(status().isForbidden());
        verify(personService, never()).findByUsername(any());
        verify(personService, never()).unlock(any(), anyString());
    }

    @Test
    public void enableCallsEnableOnPersonService() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final Person person = new Person();
        person.disable();
        person.setUsername("disabled@inbas.cz");
        when(personService.findByUsername(person.getUsername())).thenReturn(person);

        mockMvc.perform(post("/persons/status").param("username", person.getUsername()))
               .andExpect(status().isNoContent());
        verify(personService).enable(person);
    }

    @Test
    public void enableThrowsNotFoundForUnknownUsername() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final String username = "unknown@inbas.cz";
        when(personService.findByUsername(username)).thenReturn(null);

        mockMvc.perform(post("/persons/status").param("username", username)).andExpect(status().isNotFound());
        verify(personService, never()).enable(any());
    }

    @Test
    public void disableCallsDisableOnPersonService() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final Person person = new Person();
        person.setUsername("enabled@inbas.cz");
        when(personService.findByUsername(person.getUsername())).thenReturn(person);

        mockMvc.perform(delete("/persons/status").param("username", person.getUsername()))
               .andExpect(status().isNoContent());
        verify(personService).disable(person);
    }

    @Test
    public void disableThrowsNotFoundForUnknownUsername() throws Exception {
        final Person admin = Generator.getPerson();
        admin.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(admin);
        final String username = "unknown@inbas.cz";
        when(personService.findByUsername(username)).thenReturn(null);

        mockMvc.perform(delete("/persons/status").param("username", username)).andExpect(status().isNotFound());
        verify(personService, never()).disable(any());
    }

    @Test
    public void findAllSupportsJsonLdMediaType() throws Exception {
        final Person user = Generator.getPerson();
        user.addType(Vocabulary.s_c_admin);
        Environment.setCurrentUser(user);
        final List<Person> persons = generatePersons();
        when(personService.findAll()).thenReturn(persons);

        final MvcResult mvcResult =
                mockMvc.perform(get("/persons").accept(JsonLd.MEDIA_TYPE)).andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentType(), containsString(JsonLd.MEDIA_TYPE));
        final Object result = JsonUtils.fromString(mvcResult.getResponse().getContentAsString());
        assertTrue(result instanceof List);
        final List<?> lResult = (List<?>) result;
        assertEquals(persons.size(), lResult.size());
        for (int i = 0; i < persons.size(); i++) {
            final Object item = lResult.get(i);
            assertTrue(item instanceof Map);
            final Map<?, ?> mItem = (Map<?, ?>) item;
            assertEquals(persons.get(i).getUri().toString(), mItem.get(JsonLd.ID));
        }
    }

    @Test
    public void registerSupportsJsonLdMediaType() throws Exception {
        authenticateAnonymously();
        final Person p = Generator.getPerson();
        MvcResult result = mockMvc.perform(
                post("/persons").content(Environment.loadData("data/personJsonLd.json", String.class))
                                .contentType(JsonLd.MEDIA_TYPE))
                                  .andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personService).persist(captor.capture());
        assertTrue(p.nameEquals(captor.getValue()));
        verifyLocationEquals("/persons/" + p.getUsername(), result);
    }
}