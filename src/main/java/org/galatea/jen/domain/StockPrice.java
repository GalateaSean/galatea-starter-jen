package org.galatea.jen.domain;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

/**
 * This is the model/schema for the StockPrice table stored in MySQL
 * It uses an ID class called "StockPriceId"
 * Because the StockPrice table uses a composite key, it's required for a
 * separate, serializable class to be created for the key
 */
@Entity //This tells Hibernate to make a table out of this class
@Data
@Table(name="Stock_Price")
@IdClass(StockPriceId.class) //we need this and have the key in
                            // another class because it is a composite key
public class StockPrice {

    //The symbol of the company and the date is the composite key of the table
    @Id
    @Column(length = 5)
    private String symbol;

    @Id
    private Date date;

    //all the price information on the given date
    private Double open;

    private Double high;

    private Double low;

    private Double close;

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
}