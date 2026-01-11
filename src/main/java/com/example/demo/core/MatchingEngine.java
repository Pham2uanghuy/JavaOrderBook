package com.example.demo.core;

import java.util.Iterator;
import java.util.Map;

public final class MatchingEngine {

    private final OrderBook orderBook;

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void handleNewOrder(Order aggressor) {
        if (aggressor.side == Order.SIDE_BUY) {
            matchBuy(aggressor);
        } else {
            matchSell(aggressor);
        }

        if (aggressor.remainingQty > 0) {
            orderBook.addOrder(aggressor);
        }
    }

    /* BUY */

    private void matchBuy(Order buy) {
        Iterator<Map.Entry<Long, PriceLevel>> askIter = orderBook.getAskLevelsIterator();

        while (buy.remainingQty > 0 && askIter.hasNext()) {
            Map.Entry<Long, PriceLevel> entry = askIter.next();
            long askPrice = entry.getKey();

            if (askPrice > buy.price) {
                break;
            }

            PriceLevel level = entry.getValue();
            matchLevel(buy, level);

            if (level.isEmpty()) {
                askIter.remove();
            }
        }
    }

    /* SELL */

    private void matchSell(Order sell) {
        Iterator<Map.Entry<Long, PriceLevel>> bidIter = orderBook.getBidLevelsIterator();

        while (sell.remainingQty > 0 && bidIter.hasNext()) {
            Map.Entry<Long, PriceLevel> entry = bidIter.next();
            long bidPrice = entry.getKey();

            if (bidPrice < sell.price) {
                break;
            }

            PriceLevel level = entry.getValue();
            matchLevel(sell, level);

            if (level.isEmpty()) {
                bidIter.remove();
            }
        }
    }

    /* CORE MATCH */

    private void matchLevel(Order aggressor, PriceLevel level) {

        Order resting = level.head;

        while (resting != null && aggressor.remainingQty > 0) {
            long tradeQty = Math.min(aggressor.remainingQty, resting.remainingQty);

            aggressor.remainingQty = aggressor.remainingQty - tradeQty;
            resting.remainingQty = resting.remainingQty - tradeQty;

            Order next = resting.next;

            if (resting.remainingQty == 0) {
                level.remove(resting);
                orderBook.removeOrderFromLookupMap(resting.id);
            }

            resting = next;
        }
    }
}
