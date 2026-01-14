package main.parsing;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public final class ParserUtils {
    private ParserUtils() { }

    /**
     *  Utility method consisting of parsing a whole JsonNode array into a String based one.
     * @param arrayNode given JsonNode array
     * @return converted List of Strings.
     */
    public static List<String> parseStringArray(final JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(node -> result.add(node.asText()));
        }
        return result;
    }

    /**
     * Utility method consisting of parsing a whole JsonNode array into an Integer one.
     * @param arrayNode JsonNode given for conversion.
     * @return list of Integer values
     */
    public static List<Integer> parseIntegerArray(final JsonNode arrayNode) {
        List<Integer> result = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(node -> result.add(node.asInt()));
        }
        return result;
    }

    /**
     * Calculates the difference in days between two calendaristic dates
     * @param start LocalDate showing the left side of the interval
     * @param end LocalDate showing the right side of the interval
     * @return an integer representing the day difference;
     */
    public static int dayDifference(final LocalDate start, final LocalDate end) {
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
