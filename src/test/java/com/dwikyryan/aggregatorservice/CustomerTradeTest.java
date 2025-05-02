package com.dwikyryan.aggregatorservice;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;

import com.dwikyryan.aggregatorservice.domain.Ticker;
import com.dwikyryan.aggregatorservice.domain.TradeAction;
import com.dwikyryan.aggregatorservice.dto.TradeRequest;

class CustomerTradeTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerTradeTest.class);

    @Test
    void tradeSuccess() {
        mockCustomerTrade("customer-service/customer-trade-200.json", 200);

        var tradeRequest = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);

        postTrade(tradeRequest, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9780)
                .jsonPath("$.totalPrice").isEqualTo(220);
    }

    @Test
    void tradeFailure() {
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);

        var tradeRequest = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);

        postTrade(tradeRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] does not have enough funds to complete the transaction");
    }

    @Test
    void inputValidation(){
        var tradeRequest = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);

        postTrade(tradeRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] does not have enough funds to complete the transaction");
    }

    private void mockCustomerTrade(String path, int responseCode) {
        // mock stock-service price response
        var responseBody = this.resourceToString("stock-service/stock-price-200.json");
        mockServerClient
                .when(HttpRequest.request("/stock/GOOGLE"))
                .respond(HttpResponse.response(responseBody)
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON));

        // mock customer-service price response
        var customerResponseBody = this.resourceToString(path);
        mockServerClient.when(
                HttpRequest.request("/customers/1/trade")
                        .withMethod("POST")
                        .withBody(RegexBody.regex(".*\"price\":110.*")))
                .respond(
                        HttpResponse.response(customerResponseBody)
                                .withStatusCode(responseCode)
                                .withContentType(MediaType.APPLICATION_JSON));
    }

    private BodyContentSpec postTrade(TradeRequest tradeRequest, HttpStatus expectedStatus) {
        return this.client.post()
                .uri("/customers/1/trade")
                .bodyValue(tradeRequest)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(e -> log.info("{}", new String(Objects.requireNonNull(e.getResponseBody()))));
    }

}
