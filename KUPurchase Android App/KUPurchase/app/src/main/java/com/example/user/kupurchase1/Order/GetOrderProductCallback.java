package com.example.user.kupurchase1.Order;

import java.util.ArrayList;

/**
 * Created by dahye on 2016-03-23.
 */
public interface GetOrderProductCallback {
    public abstract void done(ArrayList<OrderConstructor> returnedOrders);
}
