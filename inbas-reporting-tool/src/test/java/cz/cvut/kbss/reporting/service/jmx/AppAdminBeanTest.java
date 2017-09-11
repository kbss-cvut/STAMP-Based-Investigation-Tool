package cz.cvut.kbss.reporting.service.jmx;

import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.jopa.sessions.Cache;
import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.PersonService;
import cz.cvut.kbss.reporting.service.cache.ReportCache;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.reporting.util.Constants;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class AppAdminBeanTest extends BaseServiceTestRunner {

    @Autowired
    private AppAdminBean adminBean;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private AdminListener listener;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManagerFactory emf;

    @After
    public void tearDown() {
        final File credentialsFile = new File(
                System.getProperty("user.home") + File.separator + Constants.ADMIN_CREDENTIALS_FILE);
        if (credentialsFile.exists()) {
            credentialsFile.delete();
        }
    }

    @Test
    public void invalidateCachePublishesEventThatInvalidatesCaches() throws Exception {
        final ReportDto dto = new OccurrenceReportDto();
        dto.setUri(URI.create(Vocabulary.s_c_report + "-instance123345"));
        dto.setFileNumber(IdentificationUtils.generateFileNumber());
        reportCache.put(dto);
        assertFalse(reportCache.getAll().isEmpty());
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        listener.setCountDownLatch(countDownLatch);
        countDownLatch.await(1000, TimeUnit.MILLISECONDS);
        adminBean.invalidateCaches();
        assertTrue(reportCache.getAll().isEmpty());
    }

    @Test
    public void invalidateCacheEvictsPersistenceProviderCache() {
        final Person p = persistPerson();
        final Cache cache = emf.getCache();
        assertTrue(cache.contains(Person.class, p.getUri(), new EntityDescriptor()));
        adminBean.invalidateCaches();
        assertFalse(cache.contains(Person.class, p.getUri(), new EntityDescriptor()));
    }

    @Component
    public static class AdminListener implements ApplicationListener<InvalidateCacheEvent> {

        private CountDownLatch countDownLatch;

        void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onApplicationEvent(InvalidateCacheEvent invalidateCacheEvent) {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    @Test
    public void persistsSystemAdminWhenHeDoesNotExist() {
        final Person result = personService.findByUsername(Constants.SYSTEM_ADMIN.getUsername());
        assertNotNull(result);
    }

    @Test
    public void doesNotCreateNewAdminWhenOneAlreadyExists() {
        final Person admin = personService.findByUsername(Constants.SYSTEM_ADMIN.getUsername());
        adminBean.initSystemAdmin();
        final Person result = personService.findByUsername(Constants.SYSTEM_ADMIN.getUsername());
        // We know that password is generated, so the same password means no new instance was created
        assertEquals(admin.getPassword(), result.getPassword());
    }

    @Test
    public void savesAdminLoginCredentialsIntoHiddenFileInUserHome() throws Exception {
        final Person admin = personService.findByUsername(Constants.SYSTEM_ADMIN.getUsername());
        final String home = System.getProperty("user.home");
        final File credentialsFile = new File(home + File.separator + Constants.ADMIN_CREDENTIALS_FILE);
        assertTrue(credentialsFile.exists());
        assertTrue(credentialsFile.isHidden());
        final List<String> lines = Files.lines(credentialsFile.toPath()).collect(Collectors.toList());
        assertThat(lines.get(0), containsString(admin.getUsername() + "/"));
        final String password = lines.get(0).substring(lines.get(0).indexOf('/') + 1);
        assertTrue(passwordEncoder.matches(password, admin.getPassword()));
    }
}
