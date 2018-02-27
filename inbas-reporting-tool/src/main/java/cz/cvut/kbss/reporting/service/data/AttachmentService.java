package cz.cvut.kbss.reporting.service.data;

import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.model.AbstractReport;
import cz.cvut.kbss.reporting.model.LogicalDocument;
import cz.cvut.kbss.reporting.model.Resource;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.ReportBusinessService;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;

/**
 * Manages binary file attachments (images, audio, video etc.) of reports.
 */
@Service
public class AttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

    private final ConfigReader configReader;

    private final ReportBusinessService reportService;

    @Autowired
    public AttachmentService(ConfigReader configReader, ReportBusinessService reportService) {
        this.configReader = configReader;
        this.reportService = reportService;
    }

    /**
     * Stores the specified attachment related to the specified report.
     * <p>
     * This also adds a corresponding {@link Resource} instance to the report.
     *
     * @param report   Report to which the file is attached
     * @param fileName Name of the attachment file
     * @param content  The attached file content
     * @throws AttachmentException When attachment storing fails
     */
    public void addAttachment(AbstractReport report, String fileName, InputStream content) {
        LOG.debug("Adding attachment {} to report {}.", fileName, report);
        attachFile(report, fileName, content);
        addResourceReference(report, fileName);
    }

    private void attachFile(AbstractReport report, String fileName, InputStream content) {
        final File attachmentsDir = getAttachmentsDir();
        final File targetDir = new File(generateReportAttachmentsPath(attachmentsDir, report));
        ensureDirectoryExists(targetDir);
        final File attachment = new File(targetDir + File.separator + fileName);
        ensureUnique(attachment);
        try {
            Files.copy(content, attachment.toPath());
        } catch (IOException e) {
            throw new AttachmentException("Unable to save attachment to file " + attachment.getAbsolutePath(), e);
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                throw new AttachmentException("Unable to close attachment input stream.", e);
            }
        }
    }

    private File getAttachmentsDir() {
        final String targetDirPath = configReader.getConfig(ConfigParam.ATTACHMENT_DIR, "");
        if (targetDirPath.isEmpty()) {
            throw new AttachmentException("Attachments directory not configured!");
        }
        final File targetDir = new File(targetDirPath);
        ensureDirectoryExists(targetDir);
        return targetDir;
    }

    private void ensureDirectoryExists(File targetDir) {
        if (!targetDir.exists()) {
            try {
                Files.createDirectories(targetDir.toPath());
            } catch (IOException e) {
                throw new AttachmentException("Unable to create directory " + targetDir.getAbsolutePath(), e);
            }
        }
    }

    private String generateReportAttachmentsPath(File attachmentsDir, LogicalDocument report) {
        return attachmentsDir.getAbsolutePath() + File.separator + report.getFileNumber() + File.separator +
                report.getKey();
    }

    private void ensureUnique(File file) {
        if (file.exists()) {
            throw new AttachmentException("Attachment file " + file.getName() + " already exists.");
        }
    }

    private void addResourceReference(AbstractReport report, String fileName) {
        final Resource resource = new Resource();
        resource.setReference(fileName);
        resource.setTypes(Collections.singleton(Vocabulary.s_c_SensoryData));
        report.addReference(resource);
        reportService.update(report);
    }
}
