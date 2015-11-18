package cz.cvut.kbss.inbas.audit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Classpath-based file content loader.
 */
public class FileDataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileDataLoader.class);

    /**
     * Loads data from the specified file.
     * <p>
     * The file is loaded as a classloader resource, so it should be on classpath or it should be an absolute path.
     *
     * @param fileName Name of the file to load
     * @return File contents as string
     */
    public String load(String fileName) {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IllegalArgumentException("File " + fileName + " not found.");
        }
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error("Unable to file contents.", e);
            return "";
        }
    }
}
