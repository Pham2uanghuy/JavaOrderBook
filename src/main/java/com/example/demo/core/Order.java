package com.example.demo.core;

public final class Order {
    // Side Constants
    public static final byte SIDE_BUY = 0;
    public static final byte SIDE_SELL = 1;
    // Status Constants
    public static final byte STATUS_OPEN = 0;
    public static final byte STATUS_PARTIALLY_FILLED = 1;
    public static final byte STATUS_FILLED = 2;
    public static final byte STATUS_CANCELED = 3;

    private long orderId;
    private int instrumentId;
    private long price;
    private long originalQty;
    private long remainingQty;
    private long filledQty;
    private byte side;
    private byte status;
    private long timestamp;


    public Order() {
    }


    public Order(long orderId, int instrumentId, long price,
                 long qty, byte side, long timestamp) {
        this.orderId = orderId;
        this.instrumentId = instrumentId;
        this.price = price;
        this.originalQty = qty;
        this.remainingQty = qty;
        this.filledQty = 0;
        this.side = side;
        this.status = STATUS_OPEN;
        this.timestamp = timestamp;
    }
    public void fill(long fillAmount) {
        if (fillAmount <= 0) return;

        this.filledQty += fillAmount;
        this.remainingQty -= fillAmount;

        if (this.remainingQty <= 0) {
            this.status = STATUS_FILLED;
            this.remainingQty = 0;
        } else {
            this.status = STATUS_PARTIALLY_FILLED;
        }
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(long originalQty) {
        this.originalQty = originalQty;
    }

    public long getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(long remainingQty) {
        this.remainingQty = remainingQty;
    }

    public long getFilledQty() {
        return filledQty;
    }

    public void setFilledQty(long filledQty) {
        this.filledQty = filledQty;
    }

    public byte getSide() {
        return side;
    }

    public void setSide(byte side) {
        this.side = side;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "OrderPrimitive{id=%d, side=%d, price=%d, qty=%d, remaining=%d, filled=%d, status=%d}",
                orderId, side, price, originalQty, remainingQty, filledQty, status);
    }
}
