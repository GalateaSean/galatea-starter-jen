package org.galatea.jen.entrypoint.exception;

/**
 * This is needed to catch a specific exception that could occur when the service, not the controller,
 * is working (when user enters an invalid symbol, or one that alpha vantage has no data on
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(final String symbol) {
      super(new StringBuilder().append("Stock price information for company with symbol: ")
              .append(symbol)
              .append(" was not found. Please double check and try again.")
              .toString());
  }
}
