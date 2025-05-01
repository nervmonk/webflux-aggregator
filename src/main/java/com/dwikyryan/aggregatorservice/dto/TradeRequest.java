package com.dwikyryan.aggregatorservice.dto;

import com.dwikyryan.aggregatorservice.domain.Ticker;
import com.dwikyryan.aggregatorservice.domain.TradeAction;

public record TradeRequest(Ticker ticker, TradeAction action, Integer quantity) {

}
