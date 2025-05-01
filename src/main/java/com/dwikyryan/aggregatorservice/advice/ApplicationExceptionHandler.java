package com.dwikyryan.aggregatorservice.advice;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dwikyryan.aggregatorservice.exceptions.CustomerNotFoundException;
import com.dwikyryan.aggregatorservice.exceptions.InvalidTradeRequestException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex, p -> {
            p.setType(URI.create("http://example.com/problems/customer-not-found"));
            p.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InvalidTradeRequestException.class)
    public ProblemDetail handleException(InvalidTradeRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, p -> {
            p.setType(URI.create("http://example.com/problems/invalid-trade-request"));
            p.setTitle("Invalid Trade Request");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }
}
