package cz.cvut.kbss.reporting.environment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.reporting.service.cache.ReportCache;
import cz.cvut.kbss.reporting.service.data.AttachmentService;
import cz.cvut.kbss.reporting.service.data.export.ReportExporter;
import cz.cvut.kbss.reporting.service.jmx.AppAdminBean;
import cz.cvut.kbss.reporting.service.repository.RepositoryOccurrenceReportService;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.reporting.service")
public class TestServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public OccurrenceReportService occurrenceReportService() {
        return spy(new RepositoryOccurrenceReportService());
    }

    @Bean
    public ReportExporter reportExporter() {
        return mock(ReportExporter.class);
    }

    @Bean
    public LoginTracker loginTracker() {
        return mock(LoginTracker.class);
    }

    @Bean
    public AppAdminBean appAdminBean() {
        return mock(AppAdminBean.class);
    }

    @Bean
    public ReportCache reportCache() {
        return spy(new ReportCache());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Environment.getObjectMapper();
    }

    @Bean
    @Primary
    public AttachmentService attachmentService(ConfigReader configReader) {
        return spy(new AttachmentService(configReader));
    }
}
