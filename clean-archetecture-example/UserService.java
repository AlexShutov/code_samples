package com.lodoss.examples.ca;

import com.lodoss.examples.db.User;

/**
 * Service for handling user-related stuff
 */
public interface UserService {

    /**
     * Save user to persistent storage
     * @param user
     */
    void saveUser(User user);

    /**
     * Retrieve user from persistent storage
     * @param name string name of the user
     * @return related user or null if not found
     */
    User getUserByName(String name);
}
