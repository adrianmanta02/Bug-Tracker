package main.milestone;

import com.fasterxml.jackson.databind.JsonNode;
import main.parsing.ParserUtils;

import java.time.LocalDate;
import java.util.List;

public final class MilestoneFactory {
    private MilestoneFactory() { }

    /**
     * Creates a new milestone by deserializing a json node
     * and assigning the right parameters type.
     * @param node JsonNode being deserialized
     * @return a new Milestone object
     */
    public static Milestone createMilestone(final JsonNode node) {
        final String name = node.get("name").asText();
        final String createdBy = node.get("username").asText();
        final LocalDate createdAt = LocalDate.parse(node.get("timestamp").asText());
        final LocalDate dueDate = LocalDate.parse(node.get("dueDate").asText());
        final List<String> blockingFor = ParserUtils.parseStringArray(node.get("blockingFor"));
        final List<Integer> tickets = ParserUtils.parseIntegerArray(node.get("tickets"));
        final List<String> assignedDevs = ParserUtils.parseStringArray(node.get("assignedDevs"));

        return new Milestone(name, blockingFor, dueDate, tickets,
                            assignedDevs, createdBy, createdAt);
    }
}
