package com.example.demo;

import com.example.demo.core.MatchingEngine;
import com.example.demo.core.Order;
import com.example.demo.core.OrderBook;

public class Main {

    public static void main(String[] args) {
        final int TOTAL_ORDERS = 1_000_000;
        final long PRICE = 100;

        OrderBook orderBook = new OrderBook();
        MatchingEngine engine = new MatchingEngine(orderBook);

        long startSetup = System.nanoTime();


        for (int i = 0; i < TOTAL_ORDERS; i++) {
            Order sell = new Order(
                    i,
                    (byte) 1,
                    PRICE,
                    1,
                    Order.SIDE_SELL,
                    System.nanoTime()
            );
            orderBook.addOrder(sell);
        }

        long endSetup = System.nanoTime();


        long startMatch = System.nanoTime();

        for (int i = 0; i < TOTAL_ORDERS; i++) {
            Order buy = new Order(
                    TOTAL_ORDERS + i,
                    (byte) 1,
                    PRICE,
                    1,
                    Order.SIDE_BUY,
                    System.nanoTime()
            );
            engine.handleNewOrder(buy);
        }

        long endMatch = System.nanoTime();

        System.out.println("Setup time (ms): " +
                (endSetup - startSetup) / 1_000_000);

        System.out.println("Matching time (ms): " +
                (endMatch - startMatch) / 1_000_000);

        System.out.println("Total orders matched: " + TOTAL_ORDERS);
    }
}
