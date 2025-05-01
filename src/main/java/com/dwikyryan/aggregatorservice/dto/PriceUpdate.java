package com.dwikyryan.aggregatorservice.dto;

import java.time.LocalDateTime;

import com.dwikyryan.aggregatorservice.domain.Ticker;

public record PriceUpdate(Ticker ticker, Integer price, LocalDateTime time) {

}
