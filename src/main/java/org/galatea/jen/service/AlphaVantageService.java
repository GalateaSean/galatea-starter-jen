package org.galatea.jen.service;

import org.galatea.jen.domain.rpsy.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * This class is used to query to Alpha Vantage for stock data
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AlphaVantageService {

    @Value("${alphavantage.api_key}")
    private String apiKey;

    @Value("${alphavantage.url}")
    private String url;

    @Autowired
    StockPriceRpsyService stockPriceRpsyService;

    @Autowired
    AlphaVantageClient alphaVantageClient;

    @Autowired
    private StockPriceRepository stockPriceRepository;


    @Autowired
    RestTemplate restTemplate;


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

        if (stockPriceRepository.contains(symbol)){ //not the first time we've queries for this symbol
                                                          // we want query output to be compact
            log.info("this query returned that we have symbol: {} in our database", symbol);
            stockPriceRpsyService.saveAVPrices(symbol,
                    alphaVantageClient.getCompactStockPrices(symbol,this.apiKey));

        } else { //first time querying for this symbol, we want the 20-years worth of data points to store
            log.info("this query returned that we do NOT have symbol: {} in our database", symbol);
            stockPriceRpsyService.saveAVPrices(symbol,
                    alphaVantageClient.getAllStockPrices(symbol, this.apiKey));
        }

        return stockPriceRpsyService.retrievePrices(symbol,days);
    }
}