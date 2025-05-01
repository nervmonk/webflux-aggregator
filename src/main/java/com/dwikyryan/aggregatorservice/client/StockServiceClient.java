package com.dwikyryan.aggregatorservice.client;

import java.time.Duration;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.dwikyryan.aggregatorservice.domain.Ticker;
import com.dwikyryan.aggregatorservice.dto.PriceUpdate;
import com.dwikyryan.aggregatorservice.dto.StockPriceResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StockServiceClient {

    private static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);
    private final WebClient client;
    private Flux<PriceUpdate> flux;

    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return this.client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    public Flux<PriceUpdate> priceUpdateStream() {
        if (Objects.isNull(this.flux)) {
            this.flux = this.getPriceUpdates();
        }

        return this.flux;
    }

    private Flux<PriceUpdate> getPriceUpdates() {
        return this.client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(this.retry())
                .cache(1);
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> log.error("stock service price stream call failed. retrying: {}",
                        rs.failure().getMessage()));
    }
}
