package org.galatea.jen.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
/**
 * This class serves as a mapper to alpha vantage JSON responses
 */
public class AlphaVantageMapper {

    public static final String TIME_SERIES = "Time Series (Daily)";
    public static final String OPENING_PRICE = "1. open";
    public static final String HIGHEST_PRICE = "2. high";
    public static final String LOWEST_PRICE = "3. low" ;
    public static final String CLOSING_PRICE = "4. close";
    public static final String VOLUME = "5. volume";
    public static final String ERROR_MESSAGE = "Error Message";
}
