package com.dwikyryan.aggregatorservice.dto;

import com.dwikyryan.aggregatorservice.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {

}
