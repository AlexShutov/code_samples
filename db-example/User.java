package com.lodoss.examples.db;

/**
 * Example of simple object for persisting using using
 */
@DatabaseTable(tableName = "users")
public class User {

    public static final String NAME = "name";
    public static final String EMAIL = "email";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = EMAIL)
    private String email;

    User() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return name.equals(((User) other).name);
    }

}
