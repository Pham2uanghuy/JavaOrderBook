package com.example.demo.core;

public final class PriceLevel {
    public Order head;
    Order tail;

    public void addLast(Order o) {
        if (tail == null) {
            head = tail = o;
        } else {
            tail.next = o;
            o.prev = tail;
            tail = o;
        }
    }

    public void remove(Order o) {
        if (o.prev != null) {
            o.prev.next = o.next;
        } else {
            head = o.next;
        }

        if (o.next != null) {
            o.next.prev = o.prev;
        } else {
            tail = o.prev;
        }

        o.next = o.prev = null;
    }

    public boolean isEmpty() {
        return head == null;
    }
}
