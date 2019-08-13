package org.galatea.jen.domain;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the model/schema for the StockPrice table stored in MySQL
 * It uses an ID class called "StockPriceId"
 * Because the StockPrice table uses a composite key, it's required for a
 * separate, serializable class to be created for the key
 */
@Entity //This tells Hibernate to make a table out of this class
@Table(name="Stock_Price")
@IdClass(StockPriceId.class) //we need this and have the key in
                            // another class because it is a composite key
public class StockPrice {

    //The symbol of the company and the date is the composite key of the table
    @Id
    @Column(length = 5)
    private String symbol;

    @Id
    @Column
    private Date date;

    //all the price information on the given date
    @Column
    private Double open;
    @Column
    private Double high;
    @Column
    private Double low;
    @Column
    private Double close;
    @Column
    private Integer volume;

    public StockPrice() {}

    public StockPrice(String symbol, Date date,
                      Double open, Double high,
                      Double low, Double close, Integer volume){
        this.symbol= symbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public Date getDate(){return this.date; }

    public Double getOpeningPrice(){
        return this.open;
    }

    public Double getHighestPrice(){
        return this.high;
    }

    public Double getLowestPrice(){
        return this.low;
    }

    public Double getClosingPrice(){
        return this.close;
    }

    public Integer getVolume(){
        return this.volume;
    }

    public String getDateString(){
        Date d = this.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(d);
    }

    public void setSymbol(String nSymbol){
        this.symbol = nSymbol;
    }

    public void setDate(Date nDate){
        this.date = nDate;
    }

    public void setOpeningPrice(Double nOpen){
        this.open = nOpen;
    }

    public void setHighestPrice(Double nHigh){
        this.high = nHigh;
    }

    public void setLowestPrice(Double nLow){
        this.low = nLow;
    }

    public void setClosingPrice(Double nClose){
        this.close = nClose;
    }

    public void setVolume(Integer nVolume){
        this.volume = nVolume;
    }


    public String toString(){
        return "\n\tdate: " + this.getDateString() + "\n\t\t" +
                "closing price: " + this.getClosingPrice() + "\n\t\t" +
                "highest price: " + this.getHighestPrice() + "\n\t\t" +
                "lowest price: " + this.getLowestPrice() + "\n\t\t" +
                "opening price " + this.getOpeningPrice() + "\n\t\t" +
                "trading volume: " + this.getVolume();
    }

}