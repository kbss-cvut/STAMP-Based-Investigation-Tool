package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.model.Person;

public interface PersonService extends BaseService<Person> {

    /**
     * Finds instance by its username.
     *
     * @param username Username to look for
     * @return Matching instance or {@code null}
     */
    Person findByUsername(String username);

    /**
     * Checks whether an instance with the specified username exists.
     *
     * @param username Username to look for
     * @return Whether person exists
     */
    boolean exists(String username);

    /**
     * Unlocks the specified user and sets the specified password as his new password.
     * <p>
     * Does nothing if the user was not locked.
     *
     * @param user        The user to unlock
     * @param newPassword The new password to use for the user
     */
    void unlock(Person user, String newPassword);

    /**
     * Enables the specified user account.
     * <p>
     * If the account was not disabled, this is a no-op.
     *
     * @param user The user to enable
     */
    void enable(Person user);

    /**
     * Disables the specified user account.
     * <p>
     * If the account was already disabled, this is a no-op.
     * <p>
     * Disabled account cannot be logged into and it cannot be used to view or modify report data.
     *
     * @param user The user to disable
     */
    void disable(Person user);
}
