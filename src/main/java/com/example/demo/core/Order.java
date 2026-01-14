package com.example.demo.core;

public final class Order {
    // Side Constants
    public static final byte SIDE_BUY = 0;
    public static final byte SIDE_SELL = 1;
    public static final long TICK_SIZE = 10;

    long id;
    byte instrumentId;
    int priceIndex;
    long remainingQty;
    byte side;
    long timestamp;

    Order next;
    Order prev;


    public Order() {
    }

    public Order(int i, byte i1, long price, int i2, byte sideSell, long l) {
        this.id = i;
        this.instrumentId = i1;
        this.priceIndex = toPriceIndex(price);
        this.remainingQty = i2;
        this.side = sideSell;
        this.timestamp = l;
    }

    public static int toPriceIndex(long rawPrice) {
        if (rawPrice % TICK_SIZE != 0) {
            throw new IllegalArgumentException(
                    "Price not aligned with tick size: " + rawPrice
            );
        }
        return (int) (rawPrice / TICK_SIZE);
    }

    @Override
    public String toString() {
        return String.format(
                "OrderPrimitive{id=%d, side=%d, price=%d, remaining=%d}",
                id, side, priceIndex * TICK_SIZE, remainingQty);
    }
}
