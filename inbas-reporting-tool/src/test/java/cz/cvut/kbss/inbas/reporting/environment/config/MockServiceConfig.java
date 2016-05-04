package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.service.*;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import cz.cvut.kbss.inbas.reporting.service.data.FileDataLoader;
import cz.cvut.kbss.inbas.reporting.service.data.RemoteDataLoader;
import cz.cvut.kbss.inbas.reporting.service.options.OptionsService;
import cz.cvut.kbss.inbas.reporting.service.security.PortalSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
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
    public ConfigReader configReader() {
        return new ConfigReader();
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
    public StatisticsService statisticsService() {
        return mock(StatisticsService.class);
    }

    @Bean
    public PortalSessionManager portalSessionManager() {
        return mock(PortalSessionManager.class);
    }

    @Bean
    public OptionsService optionsService() {
        return mock(OptionsService.class);
    }

    @Bean
    public ArmsService armsService() {
        return new ArmsService();
    }
}
