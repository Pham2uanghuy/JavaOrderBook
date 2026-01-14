package com.example.demo.core;

import java.util.BitSet;

public class OrderBook {
    final PriceLevel[] bids;
    final PriceLevel[] asks;

    final BitSet bidBits;
    final BitSet askBits;
    final PriceLadderConfig cfg;
    int bestBid = -1;
    int bestAsk = -1;

    public OrderBook(PriceLevel[] bids, PriceLevel[] asks, BitSet bidBits, BitSet askBits, PriceLadderConfig cfg) {
        this.bids = bids;
        this.asks = asks;
        this.bidBits = bidBits;
        this.askBits = askBits;
        this.cfg = cfg;
    }

    public void addOrder(Order order) {
        int idx = cfg.toIndex(order.priceIndex);

        PriceLevel[] book = order.side == Order.SIDE_BUY ? bids : asks;
        BitSet bits = order.side == Order.SIDE_BUY ? bidBits : askBits;

        PriceLevel level = book[idx];
        if (level == null) {
            level = new PriceLevel();
            book[idx] = level;
            bits.set(idx);

            if (order.side == Order.SIDE_BUY) {
                bestBid = Math.max(bestBid, idx);
            } else {
                bestAsk = (bestAsk == -1 || idx < bestAsk) ? idx : bestAsk;
            }
        }

        level.addLast(order);
    }

    private void removeLevel(boolean isBuy, int idx) {
        if (isBuy) {
            bidBits.clear(idx);
            if (idx == bestBid) {
                bestBid = bidBits.previousSetBit(idx - 1);
            }
        } else {
            askBits.clear(idx);
            if (idx == bestAsk) {
                bestAsk = askBits.nextSetBit(idx + 1);
            }
        }
    }


    public void removeAskLevel(int idx) {
        asks[idx] = null;
        removeLevel(false, idx);
    }

    public void removeBidLevel(int idx) {
        bids[idx] = null;
        removeLevel(true, idx);
    }
}
