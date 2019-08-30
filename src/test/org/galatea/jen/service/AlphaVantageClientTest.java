package org.galatea.jen.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Testing for AlphaVantageClient using Feign
 * Don't think this is the right way to be testing this...
 * should use Mockito or Mock Lab
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlphaVantageClientTest {

    @Autowired
    private AlphaVantageClient mockAlphaVantageClient;
    @Value("${alphavantage.api_key}")
    private String apiKey;

    @Test
    public void getCompactStockPricesTest() {
        String symbol = "TSLA";
        ResponseEntity<String> result = this.mockAlphaVantageClient.getCompactStockPrices(symbol,this.apiKey);
        assertNotNull(result);
        assertTrue(result.toString().contains("Meta Data") && result.toString().contains("TSLA"));
    }

    @Test
    public void getFullStockPricesTest() {
        String symbol = "TSLA";
        ResponseEntity<String> result = this.mockAlphaVantageClient.getAllStockPrices(symbol,this.apiKey);
        assertNotNull(result);
        assertTrue(result.toString().contains("Meta Data") && result.toString().contains("TSLA"));
    }

    @Test
    public void getCompactStockPricesFailTest() {
        String symbol = "Q";
        ResponseEntity<String> result = this.mockAlphaVantageClient.getCompactStockPrices(symbol,this.apiKey);
        assertNotNull(result);
        assertTrue(result.toString().contains("Error Message"));
    }

    @Test
    public void getFullStockPricesFailTest() {
        String symbol = "Q";
        ResponseEntity<String> result = this.mockAlphaVantageClient.getAllStockPrices(symbol,this.apiKey);
        assertNotNull(result);
        assertTrue(result.toString().contains("Error Message"));
    }
}
