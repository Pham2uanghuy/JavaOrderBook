package com.example.demo.core;

public final class Order {
    // Side Constants
    public static final byte SIDE_BUY = 0;
    public static final byte SIDE_SELL = 1;

    long id;
    byte instrumentId;
    long price;
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
        this.price = price;
        this.remainingQty = i2;
        this.side = sideSell;
        this.timestamp = l;
    }

    @Override
    public String toString() {
        return String.format(
                "OrderPrimitive{id=%d, side=%d, price=%d, remaining=%d}",
                id, side, price, remainingQty);
    }
}
