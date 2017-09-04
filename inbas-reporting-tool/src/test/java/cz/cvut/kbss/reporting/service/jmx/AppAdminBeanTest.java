package cz.cvut.kbss.reporting.service.jmx;

import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.jopa.sessions.Cache;
import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.cache.ReportCache;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppAdminBeanTest extends BaseServiceTestRunner {

    @Autowired
    private AppAdminBean adminBean;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private AdminListener listener;

    @Autowired
    private EntityManagerFactory emf;

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
}