package org.galatea.jen.domain.rpsy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.galatea.jen.domain.StockPrice;

import java.util.Date;
import java.util.List;


/**
 * This is the interface that will be automatically implemented by Spring into a Bean
 */
@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice,String>{

    @Override
    //if returned false, it means querying alpha vantage is required
    @Query(value = "SELECT CASE WHEN COUNT(*) = 1 THEN 'true' ELSE 'false' END AS BOOL " +
            "FROM stock_price " +
            "WHERE symbol = ? " +
            "AND date = CURRENT_DATE - 1", nativeQuery = true)
    boolean existsById(String symbol);

    //find and display all the stock prices specified by the user.
    //simply order the query by date and limit the # of entries
    //displayed
    @Query(value = "SELECT * " +
            "FROM stock_price " +
            "WHERE symbol = ? " +
            "ORDER BY date DESC " +
            "LIMIT ?", nativeQuery = true)
    List<StockPrice> findAllMatches(String symbol, Integer displayDays);


    @Query(value = "SELECT date " +
            "FROM stock_price " +
            "WHERE symbol = ? " +
            "ORDER BY date DESC " +
            "LIMIT 1", nativeQuery = true)
    Date retrieveMostRecentDate(String symbol);

}
