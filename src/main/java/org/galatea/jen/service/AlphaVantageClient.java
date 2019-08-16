package org.galatea.jen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A Feign Declarative REST Client to access endpoints from the Alpha Vantage API
 * to get price data for different stock symbols
 */
@FeignClient(name = "AlphaVantageClient", url = "${alphavantage.feign.url}")
public interface AlphaVantageClient {

    /**
     * This will be used for the initial query, which will return 20 years of
     * historical stock prices to be saved in the database
     * @param symbol the stock symbol
     * @param apiKey the apikey
     * @return a list of 20-years-worth of time series price data for the specified symbol
     */
    @GetMapping("/query?function=TIME_SERIES_DAILY&outputsize=full")
    ResponseEntity<String> getAllStockPrices(@RequestParam("symbol") String symbol,
                                                 @RequestParam("apikey") String apiKey);


    /**
     * This will be used for all subsequent queries to alpha vantage
     * @param symbol the stock symbol
     * @param apiKey the apikey
     * @return a list of 100-days worth of time series price data for the specified symbol
     */
    @GetMapping("/query?function=TIME_SERIES_DAILY")
    ResponseEntity<String> getCompactStockPrices(@RequestParam("symbol") String symbol,
                                           @RequestParam("apikey") String apiKey);
}
