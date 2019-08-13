package org.galatea.jen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.galatea.jen.entrypoint.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.galatea.jen.domain.rpsy.StockPriceRepository;
import org.galatea.jen.domain.StockPrice;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the service responsible for updating, deleting, retrieving
 * and adding new data entries into mySQL table "StockPrice"
 */

@Slf4j
@Service
public class StockPriceRpsyService {

    @Autowired
    private StockPriceRepository stockPriceRepository;

    private final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * This is the method that is used to check inside our database
     * to see if we have all the data wanted by the user available
     * 1 = yes
     * 0 = no
     * @param symbol
     */
    public Integer checkForWantedPrices(String symbol)throws ParseException, IOException {
        return stockPriceRepository.findMostRecent(symbol);
    }

    /**
     * Simple method that calls on the repository bean to query into db
     * @param symbol
     * @param days
     * @return
     */
    public List<String> retrievePrices(String symbol, Integer days){
        List<String> priceString = new ArrayList<>();
        priceString.add(symbol);
        List<StockPrice> prices = stockPriceRepository.findAllMatches(symbol, days);
        prices.forEach((stockPriceObj) -> {
                priceString.add(stockPriceObj.toString());
        });

        return priceString;
    }

    /**
     * This method is used to parse the returned JSON data from Alpha Vantage to only display
     * the stock prices for the number of days specified by the user
     * REMEMBER: stock market opens at 9:30am so you will not get stock data from previous day until
     * it's 9:30am...
     * @param initialRes
     * @return
     * @throws IOException
     */
    public void saveAVPrices(String symbol, ResponseEntity<String> initialRes)
                                throws IOException, ParseException {

        //create a List to store all Stock Price objects for batch save
        List<StockPrice> prices = new ArrayList<>();
        //we want this date as a check so that we don't re-save prices for every day, only save new prices
        Date currInDB = stockPriceRepository.retrieveMostRecentDate(symbol);
        String currDateDBString = null;
        if (currInDB != null){
            currDateDBString = dateFormatter.format(currInDB);
        }
        log.info("this is the current date string that is in the DB for {}: {}, ", symbol, currDateDBString);
        //create a JsonNode as the root
        JsonNode rootNode = new ObjectMapper().readTree(initialRes.getBody());


        //this is the chunk of JSON with all the stock info
        JsonNode timeSeriesStart = rootNode.get("Time Series (Daily)");

        //throw custom error if timeSeriesStart node is null - indicates that there might be a typo when
        //identifying symbol, or that the company simply does not exist
        if (timeSeriesStart == null){
            throw new EntityNotFoundException(symbol);
        }


        Iterator<String> dates = timeSeriesStart.fieldNames();

        //we want to skip today, because prices and trading volume is still changing
        dates.next();

        //now we need to loop through and get all the stock prices and save into db
        while (dates.hasNext()) {

            String dateString = dates.next();
            log.info("this is the date that should be saved: {}", dateString);
            if (dateString.equals(currDateDBString)){
                break;
            }
            JsonNode dateNode = timeSeriesStart.get(dateString);
            StockPrice spObj = new StockPrice(symbol, dateFormatter.parse(dateString),
                    Double.parseDouble(dateNode.findValue("1. open").asText()),
                    Double.parseDouble(dateNode.findValue("2. high").asText()),
                    Double.parseDouble(dateNode.findValue("3. low").asText()),
                    Double.parseDouble(dateNode.findValue("4. close").asText()),
                    Integer.parseInt(dateNode.findValue("5. volume").asText()));
            prices.add(spObj);
            log.info("We have successfully saved new object {} of {} into db",spObj.getSymbol(), spObj.getDate());
        }
        stockPriceRepository.saveAll(prices);
        log.info("Saved all new prices");
    }
}
