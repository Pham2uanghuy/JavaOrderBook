package com.example.demo.engine;

import com.example.demo.core.Order;
import com.example.demo.core.OrderBook;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MatchingEngine {

    private final OrderBook orderBook;

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void handleNewOrder(Order aggressorOrder) {
        if (aggressorOrder.getSide() == Order.SIDE_BUY) {
            processBuyOrder(aggressorOrder);
        } else {
            processSellOrder(aggressorOrder);
        }

        if (aggressorOrder.getStatus() == Order.STATUS_PARTIALLY_FILLED || aggressorOrder.getStatus() == Order.STATUS_OPEN) {
            orderBook.addOrder(aggressorOrder);
        }
    }

    private void processBuyOrder(Order aggressorOrder) {
        Iterator<Map.Entry<Long, List<Order>>> askIter = orderBook.getAskLevelsIterator();

        if (askIter != null) {
            while (aggressorOrder.getRemainingQty() > 0 && askIter.hasNext()) {
                Map.Entry<Long, ? extends Collection<Order>> askLevel = askIter.next();
                long askPrice = askLevel.getKey();
                Collection<Order> restingAsks = askLevel.getValue();

                if (aggressorOrder.getPrice() >= askPrice && restingAsks != null) {
                    processLevel(aggressorOrder, restingAsks, Order.SIDE_BUY);

                    if (restingAsks.isEmpty()) {
                        askIter.remove();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void processSellOrder(Order aggressorOrder) {
        Iterator<Map.Entry<Long, List<Order>>> bidIter = orderBook.getBidLevelsIterator();

        if (bidIter != null) {
            while (aggressorOrder.getRemainingQty() > 0 && bidIter.hasNext()) {
                Map.Entry<Long, ? extends Collection<Order>> bidLevel = bidIter.next();
                long bidPrice = bidLevel.getKey();
                Collection<Order> restingBids = bidLevel.getValue();

                if (aggressorOrder.getPrice() <= bidPrice && restingBids != null) {
                    processLevel(aggressorOrder, restingBids, Order.SIDE_SELL);

                    if (restingBids.isEmpty()) {
                        bidIter.remove();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void processLevel(Order aggressorOrder, Collection<Order> restingOrders, byte aggressorSide) {
        Iterator<Order> restingIter = restingOrders.iterator();

        while (aggressorOrder.getRemainingQty() > 0 && restingIter.hasNext()) {
            Order restingOrder = restingIter.next();

            if (restingOrder == null) {
                restingIter.remove();
                continue;
            }

            long tradeQty = Math.min(aggressorOrder.getRemainingQty(), restingOrder.getRemainingQty());

            if (tradeQty > 0) {
                aggressorOrder.fill(tradeQty);
                restingOrder.fill(tradeQty);
                if (restingOrder.getStatus() == Order.STATUS_FILLED) {
                    restingIter.remove();
                    orderBook.removeOrderFromLookupMap(restingOrder.getOrderId());
                }
                if (aggressorOrder.getStatus() == Order.STATUS_FILLED) {
                    break;
                }
            }
        }
    }
}
