package com.lodoss.examples.db;

/**
 * Example with inner object
 */
@DatabaseTable(tableName = "orders")
public class Order {

    public static final String USER_ID= "account_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, columnName = USER_ID)
    private User user;

    @DatabaseField
    private int itemNumber;

    @DatabaseField
    private int quantity;

    @DatabaseField
    private float price;

    Order() {
    }

    public Order(User user, int itemNumber, float price, int quantity) {
        this.user = user;
        this.itemNumber = itemNumber;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
