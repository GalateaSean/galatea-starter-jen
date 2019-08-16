package org.galatea.jen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.galatea.jen.domain.StockPriceId;
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
import java.time.LocalDate;
import java.util.*;
import static org.galatea.jen.domain.AlphaVantageMapper.*;

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
     * @param symbol
     */
    public boolean wantedPricesExist(String symbol) {
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        Date yesterdayDate = java.sql.Date.valueOf(yesterday);

        return stockPriceRepository.existsById(
                StockPriceId.builder().symbol(symbol).date(yesterdayDate).build());
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
        prices.forEach(stockPriceObj ->
                priceString.add(stockPriceObj.toString()));

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
        //get latest date we have prices on from db
        String newestStoredDate = this.getLatestEntry(symbol);
        //get the start of all time series
        JsonNode timeSeriesStart = this.getTimeSeriesStart(initialRes, symbol);
        //get all the dates to iterate over
        Iterator<String> dates = timeSeriesStart.fieldNames();

        //we want to skip today, because prices and trading volume is still changing
        dates.next();

        //now we need to loop through and get all the stock prices and save into db
        while (dates.hasNext()) {
            String dateString = dates.next();
            if (dateString.equals(newestStoredDate)){
                break;
        }
            JsonNode dateNode = timeSeriesStart.get(dateString);
            prices.add(StockPrice.builder()
                    .symbol(symbol)
                    .date(dateFormatter.parse(dateString))
                    .open(dateNode.findValue(OPENING_PRICE).asDouble())
                    .high(dateNode.findValue(HIGHEST_PRICE).asDouble())
                    .low(dateNode.findValue(LOWEST_PRICE).asDouble())
                    .close(dateNode.findValue(CLOSING_PRICE).asDouble())
                    .volume(dateNode.findValue(VOLUME).asInt()).build());
            log.info("StockPrice object of date: {} has been created", dateFormatter.parse(dateString));
        }
        stockPriceRepository.saveAll(prices);
        log.info("Saved all new prices");
    }


    /**
     * Helper method for parsing the JsonNode to get to the start of the time series data
     * @param initialRes
     * @param symbol
     * @return
     * @throws IOException
     */
    public JsonNode getTimeSeriesStart(ResponseEntity<String> initialRes, String symbol) throws IOException {
        //create a JsonNode as the root
        JsonNode rootNode = new ObjectMapper().readTree(initialRes.getBody());

        //throw custom error if rootNode contains an error message - indicates that there might be a typo when
        //identifying symbol, or that the company simply does not exist
        if (rootNode.has(ERROR_MESSAGE)){
            throw new EntityNotFoundException(symbol);
        }
        //this is the chunk of JSON with all the stock info
        return rootNode.get(TIME_SERIES);
    }

    /**
     * Helper method for retrieving the date of the latest prices we have stored in the database
     * @param symbol
     * @return
     */
    public String getLatestEntry(String symbol){
        //we want this date as a check so that we don't re-save prices for every day, only save new prices
        Date currInDB = stockPriceRepository.retrieveMostRecentDate(symbol);
        String currDateDBString = null;
        if (currInDB != null){
            currDateDBString = dateFormatter.format(currInDB);
        } //if it is null, then it means we don't have data on this symbol
        log.info("this is the current date string that is in the DB for {}: {}, ", symbol, currDateDBString);
        return currDateDBString;
    }
}