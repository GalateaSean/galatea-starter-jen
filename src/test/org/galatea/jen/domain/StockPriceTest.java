package org.galatea.jen.domain;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import static org.junit.Assert.assertEquals;


/**
 * JUnit test for StockPrice objects
 */
@SpringBootTest
public class StockPriceTest {

    //used to validate bean instances
    private static Validator validator;
    private final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void validStockPrice() throws ParseException {
        StockPrice stockPrice = StockPrice.builder()
                .symbol("TSLA")
                .date(dateFormatter.parse("2019-08-28"))
                .high(83.3300)
                .low(82.2400)
                .close(83.1600)
                .volume(349100)
                .build();

        Set<ConstraintViolation<StockPrice>> constraintViolations = validator.validate(stockPrice);

        assertEquals(0, constraintViolations.size());
    }
}
