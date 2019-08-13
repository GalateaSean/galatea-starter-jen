package org.galatea.jen.entrypoint;

import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import java.lang.*;
import java.text.ParseException;
import org.galatea.jen.domain.StockPrice;
import org.galatea.jen.service.StockPriceRpsyService;
import org.galatea.jen.service.AVRequestService;


/**
 * This is the controller that passes on user parameters to services to obtain stock prices
 */
@RestController
@Slf4j
public class RequestController {

    //let Spring do its thing and initialize the two services
    @Autowired
    StockPriceRpsyService stockPriceRpsyService;

    @Autowired
    AVRequestService avRequestService;

    /**
     * This method passes the user input to StockPriceRpsyService to check for data
     * in DB - if not StockPriceRpsyService calls AVRequestService to query Alpha Vantage API
     * and eventually returns desired data back to controller to return to user
     */
    @GetMapping(value = "/prices",
            produces =  MediaType.APPLICATION_JSON_VALUE)
    //we want to take in two parameters - the ticker and the number of days of data to display
    public List<StockPrice> getPrices(
            @RequestParam(value = "symbol" , required=true) String symbol,
            @RequestParam(value = "days", required=true) Integer days)
            throws IOException, ParseException {

        Integer found = stockPriceRpsyService.checkForWantedPrices(symbol);

        if (found == 1){ //we have all the data we want in db (*excluding data from today)
                        //immediately fetch
            return stockPriceRpsyService.retrievePrices(symbol,days);
        } //we don't have all data we want, query AV, save in db, fetch
        return avRequestService.getStockData(symbol,days);
    }
}