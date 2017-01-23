package com.lodoss.examples.db;

import android.content.Context;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Example of storing data using ORMLite
 */

public class UsageExample {

    private Dao<User, Integer> mAccountDao;
    private Dao<Order, Integer> mOrderDao;

    /**
     * Setup our database and DAOs
     */
    private void setupDatabase(Context context) throws Exception {

        mAccountDao = DaoManager.createDao(context, User.class);
        mOrderDao = DaoManager.createDao(context, Order.class);

        // if you need to create the table
        TableUtils.createTable(context, User.class);
        TableUtils.createTable(context, Order.class);
    }

    private void readWriteData() throws Exception {
        // create new user
        String name = "Lodoss Team";
        User user = new User(name);

        // save the user to the db
        mAccountDao.create(user);

        // make new Order for the User
        // Lodoss team bought 2 of item #21312 for a price of $12.32
        int quantity1 = 2;
        int itemNumber1 = 21312;
        float price1 = 12.32F;
        Order o1 = new Order(user, itemNumber1, price1, quantity1);
        mOrderDao.create(o1);

        // make another Order for the User
        // Lodoss also bought 1 of item #785 for a price of $7.98
        int quantity2 = 1;
        int itemNumber2 = 785;
        float price2 = 7.98F;
        Order o2 = new Order(user, itemNumber2, price2, quantity2);
        mOrderDao.create(o2);

        // make a query using the QueryBuilder object
        QueryBuilder<Order, Integer> queryBuilder = mOrderDao.queryBuilder();
        // find all the orders that match the user
        // ORM extracts the id from the user for the query automatically
        queryBuilder.where().eq(Order.USER_ID, user);
        List<Order> orderList = mOrderDao.query(queryBuilder.prepare());

        assertEquals("Should have found both of the orders for the user", 2, orderList.size());
        assertTrue(mOrderDao.objectsEqual(o1, orderList.get(0)));
        assertTrue(mOrderDao.objectsEqual(o2, orderList.get(1)));

        assertEquals(user.getId(), orderList.get(1).getUser().getId());
        assertEquals(user.getId(), orderList.get(0).getUser().getId());
        assertNull(orderList.get(1).getUser().getName());
        assertNull(orderList.get(0).getUser().getName());

        assertEquals(1, mAccountDao.refresh(orderList.get(0).getUser()));
        assertEquals(1, mAccountDao.refresh(orderList.get(1).getUser()));
        
        assertEquals(user.getName(), orderList.get(0).getUser().getName());
        assertEquals(user.getName(), orderList.get(1).getUser().getName());
    }
}
