package com.dwikyryan.aggregatorservice.dto;

import com.dwikyryan.aggregatorservice.domain.Ticker;
import com.dwikyryan.aggregatorservice.domain.TradeAction;

public record StockTradeResponse(Integer customerId, Ticker ticker, Integer price, Integer quantity, TradeAction action,
        Integer totalPrice, Integer balance) {

}
