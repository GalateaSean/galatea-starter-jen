package price.checker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import price.checker.domain.StockPrice;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to query to Alpha Vantage for stock data
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AVRequestService {

    private final String API_KEY = "KB7DSCK48G00WIRE";

    @Autowired
    StockPriceRpsyService StockPriceRpsyService;


    /**
     * This method is used when we have confirmed that the price data the the user
     * wants does not exist in our database. This method queries Alpha Vantage and
     * passes a ResponseEntity object to StockPriceRpsyService to save as StockPrice Objects
     * @param symbol
     * @param days
     * @return
     * @throws IOException
     */
    public String getStockData (String symbol, Integer days)
                                        throws IOException, ParseException {
        //the alpha vantage querying url with ticker is a variable
        final String AlphaVantageUri = "https://www.alphavantage.co/query?" +
                "function=TIME_SERIES_DAILY&" +
                "symbol={stock}&" +
                "outputsize=full&" +
                "apikey="+API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        //use the restTemplate to submit a GET request with user variables
        ResponseEntity<String> initialRes = restTemplate.getForEntity(AlphaVantageUri, String.class, symbol);

        StockPriceRpsyService.saveAVPrices(symbol, initialRes);
        return StockPriceRpsyService.retrievePrices(symbol,days);
    }
}
