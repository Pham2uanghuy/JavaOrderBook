package com.example.demo.core;

import java.util.*;


public final class OrderBook {

    // BUY: high -> low, SELL: low -> high
    private final NavigableMap<Long, PriceLevel> bidLevels = new TreeMap<>(Comparator.reverseOrder());

    private final NavigableMap<Long, PriceLevel> askLevels = new TreeMap<>();

    // orderId -> Order (O(1) cancel / lookup)
    private final Map<Long, Order> ordersById = new HashMap<>();

    public void addOrder(Order order) {
        NavigableMap<Long, PriceLevel> book =
                order.side == Order.SIDE_BUY ? bidLevels : askLevels;

        PriceLevel level = book.computeIfAbsent(order.price, p -> new PriceLevel());

        level.addLast(order);
        ordersById.put(order.id, order);
    }

    public void removeOrder(Order order) {
        NavigableMap<Long, PriceLevel> book =
                order.side == Order.SIDE_BUY ? bidLevels : askLevels;

        PriceLevel level = book.get(order.price);
        if (level == null) {
            return;
        }

        level.remove(order);

        if (level.isEmpty()) {
            book.remove(order.price);
        }

        ordersById.remove(order.id);
    }

    public Iterator<Map.Entry<Long, PriceLevel>> getBidLevelsIterator() {
        return bidLevels.entrySet().iterator();
    }


    public Iterator<Map.Entry<Long, PriceLevel>> getAskLevelsIterator() {
        return askLevels.entrySet().iterator();
    }


    public Order getOrderDetail(long orderId) {
        return ordersById.get(orderId);
    }


    public void removeOrderFromLookupMap(long orderId) {
        ordersById.remove(orderId);
    }


    public void clear() {
        bidLevels.clear();
        askLevels.clear();
        ordersById.clear();
    }
}
