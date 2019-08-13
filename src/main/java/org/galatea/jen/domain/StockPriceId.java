package org.galatea.jen.domain;


import java.io.Serializable;
import java.util.Date;


/**
 * This is the class containing the two fields that makes up the
 * composite key of the StockPrice table that is stored in mySQL
 * in a database called daily_stock_prices
 */
public class StockPriceId implements Serializable {

    private String symbol;
    private Date date;

    public StockPriceId(){}

    public StockPriceId(String symbol, Date date){
        this.symbol = symbol;
        this.date = date;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public Date getDate(){
        return this.date;
    }
}