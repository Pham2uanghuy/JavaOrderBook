package com.example.demo.core.impl;

import com.example.demo.core.Order;
import com.example.demo.core.OrderBook;

import java.util.*;


public class SimpleOrderBook implements OrderBook {

    // Map to store bid and ask levels
    private final NavigableMap<Long, List<Order>> bidLevels;
    private final NavigableMap<Long, List<Order>> askLevels;

    // Map to quickly find an order by its ID
    private final Map<Long, Order> ordersById;

    public SimpleOrderBook() {
        this.bidLevels = new TreeMap<>(Comparator.reverseOrder()); // bids high -> low
        this.askLevels = new TreeMap<>(); // asks low -> high
        this.ordersById = new HashMap<>();
    }

    @Override
    public void addOrder(Order order) {
        NavigableMap<Long, List<Order>> targetBook =
                (order.getSide() == Order.SIDE_BUY) ? bidLevels : askLevels;

        targetBook
                .computeIfAbsent(order.getPrice(), k -> new LinkedList<>())
                .add(order);

        // Store the order by ID for quick lookups and updates
        ordersById.put(order.getOrderId(), order);
    }

    @Override
    public void removeOrder(Order order) {
        NavigableMap<Long, List<Order>> targetBook =
                (order.getSide() == Order.SIDE_BUY) ? bidLevels : askLevels;

        List<Order> ordersAtLevel = targetBook.get(order.getPrice());
        if (ordersAtLevel != null) {
            ordersAtLevel.remove(order);
            if (ordersAtLevel.isEmpty()) {
                targetBook.remove(order.getPrice());
            }
        }
        ordersById.remove(order.getOrderId());
    }

    @Override
    public Iterator<Map.Entry<Long, List<Order>>> getBidLevelsIterator() {
        return (bidLevels != null) ? bidLevels.entrySet().iterator() : Collections.emptyIterator();
    }

    @Override
    public Iterator<Map.Entry<Long, List<Order>>> getAskLevelsIterator() {
        return (askLevels != null) ? askLevels.entrySet().iterator() : Collections.emptyIterator();
    }

    @Override
    public Order getOrderDetail(long orderId) {
        return ordersById.get(orderId);
    }

    @Override
    public void clear() {
        bidLevels.clear();
        askLevels.clear();
        ordersById.clear();
    }

    @Override
    public void removeOrderFromLookupMap(long orderId) {
        ordersById.remove(orderId);
    }


}
