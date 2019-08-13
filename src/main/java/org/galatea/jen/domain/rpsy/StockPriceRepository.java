package org.galatea.jen.domain.rpsy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.galatea.jen.domain.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


/**
 * This is the interface that will be automatically implemented by Spring into a Bean
 */
@Repository
//JpaRepository<{Entity Class}, {Data Type of Primary Key Field}>
public interface StockPriceRepository extends JpaRepository<StockPrice,String>{

    //symbol parameter will be assigned to the query parameter with index 1,
    //if returned integer is 0, it means querying alpha vantage is required
    @Query(value = "SELECT COUNT(*) " +
            "FROM stock_price " +
            "WHERE symbol = ?1 " +
            "AND date = CURRENT_DATE - 1", nativeQuery = true)
    Integer findMostRecent(String symbol);


    //find and display all the stock prices specified by the user.
    //simply order the query by date and limit the # of entries
    //displayed
    @Query(value = "SELECT * " +
            "FROM stock_price " +
            "WHERE symbol = ?1 " +
            "ORDER BY date DESC " +
            "LIMIT ?2", nativeQuery = true)
    List<StockPrice> findAllMatches(String symbol, Integer displayDays);


    @Query(value = "SELECT date " +
            "FROM stock_price " +
            "WHERE symbol = ?1 " +
            "ORDER BY date DESC " +
            "LIMIT 1", nativeQuery = true)
    Date retrieveMostRecentDate(String symbol);

}
