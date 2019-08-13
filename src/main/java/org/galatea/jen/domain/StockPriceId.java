package org.galatea.jen.domain;



import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * This is the class containing the two fields that makes up the
 * composite key of the StockPrice table that is stored in mySQL
 * in a database called daily_stock_prices
 */
@Data
public class StockPriceId implements Serializable {

    private String symbol;
    private Date date;

    public StockPriceId(){}

}