package cz.cvut.kbss.inbas.reporting.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.reporting.persistence")
public class PersistenceConfig {
}
