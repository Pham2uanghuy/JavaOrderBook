package com.example.demo.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface OrderBook {
    /**
     * Adds a new order to the order book.
     */
    void addOrder(Order order);


    /**
     * Removes an order from the order book.
     */
    void removeOrder(Order order);

    /**
     * @return An iterator over bid price levels.
     */
    Iterator<Map.Entry<Long, List<Order>>> getBidLevelsIterator();

    /**
     * @return An iterator over ask price levels.
     */
    Iterator<Map.Entry<Long, List<Order>>> getAskLevelsIterator();

    /**
     * Gets the details of a specific order by its ID.
     *
     * @return The order object, or null if not found.
     */
    Order getOrderDetail(long orderId);

    /**
     * Clears all orders from the order book.
     */
    void clear();

    /**
     * Remove order from quick lookup map
     */
    void removeOrderFromLookupMap(long orderId);

}
