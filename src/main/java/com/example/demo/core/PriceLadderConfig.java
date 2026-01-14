package com.example.demo.core;

public class PriceLadderConfig {
    public final int size;
    final int minPriceIndex;
    final int maxPriceIndex;

    public PriceLadderConfig(int min, int max) {
        this.minPriceIndex = min;
        this.maxPriceIndex = max;
        this.size = max - min + 1;
    }

    public int toIndex(int priceIndex) {
        return priceIndex - minPriceIndex;
    }
}
