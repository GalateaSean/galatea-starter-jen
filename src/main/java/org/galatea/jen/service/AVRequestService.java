package org.galatea.jen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * This class is used to query to Alpha Vantage for stock data
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AVRequestService {

    @Value("${alphavantage.api_key}")
    private String apiKey;

    @Value("${alphavantage.url}")
    private String url;

    @Autowired
    StockPriceRpsyService stockPriceRpsyService;


    /**
     * This method is used when we have confirmed that the price data the the user
     * wants does not exist in our database. This method queries Alpha Vantage and
     * passes a ResponseEntity object to StockPriceRpsyService to save as StockPrice Objects
     * @param symbol
     * @param days
     * @return
     * @throws IOException
     */
    public List<String> getStockData (String symbol, Integer days)
                                        throws IOException, ParseException {

        String alphaVantageUrl = String.format(this.url, symbol ,this.apiKey);

        RestTemplate restTemplate = new RestTemplate();
        //use the restTemplate to submit a GET request with user variables
        ResponseEntity<String> initialRes = restTemplate.getForEntity(alphaVantageUrl, String.class);

        stockPriceRpsyService.saveAVPrices(symbol, initialRes);
        return stockPriceRpsyService.retrievePrices(symbol,days);
    }
}
