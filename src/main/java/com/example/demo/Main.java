package com.example.demo;

import com.example.demo.core.*;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {

        final int TOTAL_ORDERS = 1_000_000;
        final long RAW_PRICE = 100; // must be aligned with tick
        final byte INSTRUMENT = 1;

        /* PRICE LADDER CONFIG */

        int minPriceIndex = 0;      // 0 * tick = 0
        int maxPriceIndex = 1_000;  // 1000 * tick = 10_000

        PriceLadderConfig cfg =
                new PriceLadderConfig(minPriceIndex, maxPriceIndex);

        /* ORDER BOOK */

        OrderBook orderBook = new OrderBook(
                new PriceLevel[cfg.size],
                new PriceLevel[cfg.size],
                new BitSet(cfg.size),
                new BitSet(cfg.size),
                cfg
        );

        MatchingEngine engine = new MatchingEngine(orderBook);

        /* SETUP SELL ORDERS */

        long startSetup = System.nanoTime();

        for (int i = 0; i < TOTAL_ORDERS; i++) {
            Order sell = new Order(
                    i,
                    INSTRUMENT,
                    RAW_PRICE,
                    1,
                    Order.SIDE_SELL,
                    System.nanoTime()
            );
            orderBook.addOrder(sell);
        }

        long endSetup = System.nanoTime();

        /* MATCH BUY ORDERS */

        long startMatch = System.nanoTime();

        for (int i = 0; i < TOTAL_ORDERS; i++) {
            Order buy = new Order(
                    TOTAL_ORDERS + i,
                    INSTRUMENT,
                    RAW_PRICE,
                    1,
                    Order.SIDE_BUY,
                    System.nanoTime()
            );
            engine.handleNewOrder(buy);
        }

        long endMatch = System.nanoTime();

        /* RESULT */

        System.out.println("Setup time (ms): " +
                (endSetup - startSetup) / 1_000_000.0);

        System.out.println("Matching time (ms): " +
                (endMatch - startMatch) / 1_000_000.0);

        System.out.println("Total orders matched: " + TOTAL_ORDERS);
    }
}
