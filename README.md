# Java Order Book Implementations

This repository explores multiple **single-threaded, in-memory Order Book** implementations in Java.

---

## Core Components

### 1. Order Book

An **Order Book** is a real-time ledger of all outstanding buy and sell orders for a financial instrument.

- **Bid Side (Buy orders):**  
  Sorted **descending by price** → highest price = *best bid*.

- **Ask Side (Sell orders):**  
  Sorted **ascending by price** → lowest price = *best ask*.

#### Data Structures

- **Price Levels** → `NavigableMap<Long, List<PrimitiveOrder>>`  
  - Key: Price level  
  - Value: FIFO list of orders at that price  

- **Order Lookup** → `Map<Long, PrimitiveOrder>`  
  - Key: `orderId`  
  - Value: Order reference (for O(1) cancellation & modification)

---

### 2. Matching Logic

The **Matching Logic** determines whether a new order can be executed against resting orders.

#### Price-Time Priority

1. **Price Priority:**  
   - Higher-priced buys match first  
   - Lower-priced sells match first  

2. **Time Priority:**  
   - If same price → earliest order gets matched first