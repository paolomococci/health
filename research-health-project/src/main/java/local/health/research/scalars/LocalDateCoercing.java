package local.health.research.scalars;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

/**
 * LocalDateCoercing implements GraphQL's Coercing interface to
 * translate between the Java LocalDate type and a GraphQL
 * string representation. The GraphQL schema defines a
 * custom scalar that accepts dates in ISO-8601 format
 * (yyyy-MM-dd). This class provides three essential
 * conversions:
 * 1. parseValue - convert a client value (e.g., from
 * variables) into a LocalDate instance.
 * 2. serialize - convert a Java LocalDate returned by a
 * resolver into a string that the GraphQL response can
 * send to the client.
 * 3. parseLiteral - convert a hard-coded literal from
 * the GraphQL query into a LocalDate.
 *
 * The implementation validates input format and throws
 * GraphQL specific coercing exceptions when parsing fails,
 * enabling the GraphQL engine to return clear error
 * messages to the client.
 */
public class LocalDateCoercing implements Coercing<LocalDate, String> {

  /**
   * parseValue is called when a client passes a variable
   * (e.g., a date string) to a mutation or query. It
   * expects the input to be a String. The string is parsed
   * using LocalDate.parse which requires ISO-8601 format.
   * If parsing fails, a CoercingParseValueException is thrown
   * with a helpful message. If the input is not a String,
   * a generic parsing exception is thrown.
   */
  @Override
  public LocalDate parseValue(Object input) {
    if (input instanceof String) {
      try {
        return LocalDate.parse((String) input);
      } catch (DateTimeParseException e) {
        throw new CoercingParseValueException("Invalid Date value: expected ISO-8601 (yyyy-MM-dd)");
      }
    }
    throw new CoercingParseValueException("Date cannot be parsed from type: " + input.getClass().getSimpleName());
  }

  /**
   * serialize is invoked when a resolver returns a value
   * that needs to be sent back to the GraphQL client. The
   * method expects the data fetcher result to be a LocalDate.
   * It returns the date as its ISO string representation.
   * If the result is not a LocalDate, a CoercingSerializeException
   * is thrown to indicate a type mismatch.
   */
  @Override
  public String serialize(Object dataFetcherResult) {
    if (dataFetcherResult instanceof LocalDate) {
      return ((LocalDate) dataFetcherResult).toString();
    }
    throw new CoercingSerializeException("Expected a LocalDate object but found: " + dataFetcherResult);
  }

  /**
   * parseLiteral is used when the client provides a hard-coded
   * literal in the GraphQL query (e.g., date:"2023-01-01").
   * The input is expected to be a StringValue node. The
   * string value is parsed into a LocalDate. If the literal
   * is not a string or cannot be parsed, a CoercingParseLiteralException
   * is thrown to inform the client of the error.
   */
  @Override
  public LocalDate parseLiteral(Object input) {
    if (input instanceof StringValue) {
      try {
        return LocalDate.parse(((StringValue) input).getValue());
      } catch (DateTimeParseException e) {
        throw new CoercingParseLiteralException("Invalid Date literal: expected ISO-8601 (yyyy-MM-dd)");
      }
    }
    throw new CoercingParseLiteralException("Date literal must be a string value");
  }
}
