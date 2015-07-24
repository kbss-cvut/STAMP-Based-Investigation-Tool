package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ledvima1
 */
@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit")
@Import({WebAppConfig.class})
public class AppConfig {
}
