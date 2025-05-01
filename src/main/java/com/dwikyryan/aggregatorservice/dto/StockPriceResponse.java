package com.dwikyryan.aggregatorservice.dto;

import com.dwikyryan.aggregatorservice.domain.Ticker;

public record StockPriceResponse(Ticker ticker, Integer price) {

}
