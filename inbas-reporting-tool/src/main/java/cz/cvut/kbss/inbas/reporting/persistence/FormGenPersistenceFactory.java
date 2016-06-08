package cz.cvut.kbss.inbas.reporting.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

import static cz.cvut.kbss.inbas.reporting.util.ConfigParam.DRIVER;
import static cz.cvut.kbss.inbas.reporting.util.ConfigParam.FORM_GEN_REPOSITORY_URL;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.*;

@Configuration
@PropertySource("classpath:config.properties")
public class FormGenPersistenceFactory {

    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean(name = "formGen")
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>(PersistenceFactory.getDefaultParams());
        properties.put(ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(FORM_GEN_REPOSITORY_URL.toString()));
        properties.put(DATA_SOURCE_CLASS, environment.getProperty(DRIVER.toString()));
        properties.put(CACHE_ENABLED, Boolean.FALSE.toString());
        this.emf = Persistence.createEntityManagerFactory("formGenPU", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }
}
