package cz.cvut.kbss.reporting.exception;

/**
 * Exception thrown when access to other application's web services fails.
 */
public class WebServiceIntegrationException extends ReportingToolException {

    public WebServiceIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
