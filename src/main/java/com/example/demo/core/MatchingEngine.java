package com.example.demo.core;


public final class MatchingEngine {

    private final OrderBook book;

    public MatchingEngine(OrderBook book) {
        this.book = book;
    }

    public void handleNewOrder(Order aggressor) {
        if (aggressor.side == Order.SIDE_BUY) {
            matchBuy(aggressor);
        } else {
            matchSell(aggressor);
        }

        // còn dư thì vào book
        if (aggressor.remainingQty > 0) {
            book.addOrder(aggressor);
        }
    }

    /* ================= BUY ================= */

    private void matchBuy(Order buy) {
        int ask = book.bestAsk;

        while (ask != -1
                && ask <= buy.priceIndex
                && buy.remainingQty > 0) {

            PriceLevel level = book.asks[ask];
            matchLevel(buy, level);

            if (level.isEmpty()) {
                book.removeAskLevel(ask);
                ask = book.bestAsk;
            } else {
                break;
            }
        }
    }

    /* ================= SELL ================= */

    private void matchSell(Order sell) {
        int bid = book.bestBid;

        while (bid != -1
                && bid >= sell.priceIndex
                && sell.remainingQty > 0) {

            PriceLevel level = book.bids[bid];
            matchLevel(sell, level);

            if (level.isEmpty()) {
                book.removeBidLevel(bid);
                bid = book.bestBid;
            } else {
                break;
            }
        }
    }

    /* ================= CORE MATCH ================= */

    private void matchLevel(Order aggressor, PriceLevel level) {
        Order resting = level.head;

        while (resting != null && aggressor.remainingQty > 0) {
            long traded = Math.min(aggressor.remainingQty, resting.remainingQty);

            aggressor.remainingQty -= traded;
            resting.remainingQty -= traded;

            Order next = resting.next;

            if (resting.remainingQty == 0) {
                level.remove(resting);
            }

            resting = next;
        }
    }
}
