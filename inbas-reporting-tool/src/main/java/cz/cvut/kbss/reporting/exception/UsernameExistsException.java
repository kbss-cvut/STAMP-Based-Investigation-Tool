package cz.cvut.kbss.reporting.exception;

/**
 * Thrown when trying to add an user whose username already exists in the repository.
 */
public class UsernameExistsException extends ReportingToolException {

    public UsernameExistsException(String message) {
        super(message);
    }
}
