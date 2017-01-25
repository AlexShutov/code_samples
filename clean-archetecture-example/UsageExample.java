package com.lodoss.examples.ca;


import com.lodoss.examples.db.User;

public class UsageExample {

    @Inject
    UserService userService;

    public static void main(String[] args){
        /* initialize DI container and inject dependencies */

        User user = new User("Lodoss", "hello@lodoss.team");
        userService.saveUser(user);

        User foundUser = userService.getUserByName("Lodoss");

        assert foundUser == user;
    }
}
