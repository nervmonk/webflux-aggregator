package com.dwikyryan.aggregatorservice.dto;

import com.dwikyryan.aggregatorservice.domain.Ticker;
import com.dwikyryan.aggregatorservice.domain.TradeAction;

public record StockTradeRequest(Ticker ticker, Integer price, Integer quantity, TradeAction action) {
    public Integer totalPrice() {
        return price * quantity;
    }
}
