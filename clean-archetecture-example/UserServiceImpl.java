package com.lodoss.examples.ca;

import com.lodoss.examples.db.User;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of user service.
 * This is just an example, but implementation may be any necessary, i.e. saving to DB,
 * to cache, to remote server, etc.
 */
public class UserServiceImpl implements UserService {

    private List<User> userList;

    public UserServiceImpl() {
        userList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUser(User user) {
        userList.add(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByName(String name) {
        for(int i = 0; i < userList.size(); i++){
            User currentUser = userList.get(i);
            if(currentUser.getName() == name){
                return currentUser;
            }
        }
        return null;
    }
}
