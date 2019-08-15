package org.galatea.jen.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * This is the class containing the two fields that makes up the
 * composite key of the StockPrice table that is stored in mySQL
 * in a database called daily_stock_prices
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceId implements Serializable {

    private String symbol;
    private Date date;

}