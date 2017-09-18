package cz.cvut.kbss.reporting.environment.config;

import cz.cvut.kbss.reporting.service.*;
import cz.cvut.kbss.reporting.service.data.FileDataLoader;
import cz.cvut.kbss.reporting.service.data.RemoteDataLoader;
import cz.cvut.kbss.reporting.service.data.export.ReportExporter;
import cz.cvut.kbss.reporting.service.factory.OccurrenceReportFactory;
import cz.cvut.kbss.reporting.service.formgen.FormGenService;
import cz.cvut.kbss.reporting.service.options.OptionsService;
import cz.cvut.kbss.reporting.service.search.SearchService;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class MockServiceConfig {

    @Bean
    public OccurrenceService occurrenceService() {
        return mock(OccurrenceService.class);
    }

    @Bean
    public OrganizationService organizationService() {
        return mock(OrganizationService.class);
    }

    @Bean
    public PersonService personService() {
        return mock(PersonService.class);
    }

    @Bean(name = "cachingReportBusinessService")
    public ReportBusinessService reportService() {
        return mock(ReportBusinessService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ConfigReader configReader(Environment environment) {
        return new ConfigReader(environment);
    }

    @Bean(name = "localDataLoader")
    public FileDataLoader fileDataLoader() {
        return new FileDataLoader();
    }

    @Bean(name = "remoteDataLoader")
    public RemoteDataLoader remoteDataLoader() {
        return mock(RemoteDataLoader.class);
    }

    @Bean
    public SPARQLService statisticsService() {
        return mock(SPARQLService.class);
    }

    @Bean
    public OptionsService optionsService() {
        return mock(OptionsService.class);
    }

    @Bean
    public SearchService searchService() {
        return mock(SearchService.class);
    }

    @Bean
    public FormGenService formGenService() {
        return mock(FormGenService.class);
    }

    @Bean
    public OccurrenceReportFactory occurrenceReportFactory() {
        return mock(OccurrenceReportFactory.class);
    }

    @Bean
    public ReportExporter reportExporter() {
        return mock(ReportExporter.class);
    }

    @Bean
    public SecurityUtils securityUtils() {
        return mock(SecurityUtils.class);
    }

    @Bean
    public LoginTracker loginTracker() {
        return mock(LoginTracker.class);
    }
}
