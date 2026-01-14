package main.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import main.ticket.enums.bug.BugFrequency;
import main.ticket.enums.bug.BugSeverity;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketType;
import main.ticket.enums.feature.BusinessValue;
import main.ticket.enums.feature.CustomerDemand;

import java.time.LocalDate;

public final class TicketFactory {
    private TicketFactory() { }

    /**
     * Creates a Ticket instance with a set of parameters, both mandatory and optional.
     * Provides three different strategies to create a subtype of Ticket, because
     * fields differ across those types.
     * @param params a JsonNode of parameters
     * @param ticketId integer representing the id the ticket will have
     * @param businessPriority {@link BusinessPriority} value
     * @param createdAt LocalDate showing the creation date.
     * @return Ticket instance.
     */
    public static Ticket createTicket(final JsonNode params,
                               final int ticketId, final BusinessPriority businessPriority,
                               final LocalDate createdAt) {
        final String type = params.get("type").asText();
        TicketType ticketType = TicketType.fromString(type);

        return switch (ticketType) {
            case TicketType.BUG -> createBugTicket(params, ticketId, businessPriority, createdAt);
            case TicketType.FEATURE_REQUEST -> createFeatureRequestTicket(params, ticketId,
                    businessPriority, createdAt);
            case TicketType.UI_FEEDBACK -> createUIFeedbackTicket(params, ticketId,
                    businessPriority, createdAt);
        };
    }

    private static BugTicket createBugTicket(final JsonNode params, final int ticketId,
                                             final BusinessPriority businessPriority,
                                             final LocalDate createdAt) {
        final String title = params.get("title").asText();
        final String reportedBy = params.get("reportedBy").asText();
        final ExpertiseArea expertiseArea = ExpertiseArea.fromString(params.get("expertiseArea")
                .asText());
        final String expectedBehavior = params.get("expectedBehavior").asText();
        final String actualBehavior = params.get("actualBehavior").asText();
        final BugFrequency bugFrequency = BugFrequency.fromString(params.get("frequency").asText());
        final BugSeverity bugSeverity = BugSeverity.fromString(params.get("severity").asText());

        BugTicket.BugTicketBuilder bugTicketBuilder = new BugTicket.BugTicketBuilder(
                ticketId,
                TicketType.BUG,
                title,
                businessPriority,
                expertiseArea,
                expectedBehavior,
                actualBehavior,
                bugFrequency,
                bugSeverity,
                createdAt
        ).reportedBy(reportedBy);

        if (params.has("description") && !params.get("description").isNull()) {
            bugTicketBuilder.description(params.get("description").asText());
        }

        if (params.has("environment") && !params.get("environment").isNull()) {
            bugTicketBuilder.environment(params.get("environment").asText());
        }

        if (params.has("errorCode") && !params.get("errorCode").isNull()) {
            bugTicketBuilder.errorCode(Integer.parseInt(params.get("errorCode").asText()));
        }

        return bugTicketBuilder.build();
    }

    private static FeatureRequestTicket createFeatureRequestTicket(final JsonNode params,
                                                                   final int ticketId,
                                                                   final BusinessPriority
                                                                            businessPriority,
                                                                   final LocalDate createdAt) {
        final String title = params.get("title").asText();
        final ExpertiseArea expertiseArea = ExpertiseArea.fromString(params.get("expertiseArea")
                .asText());
        final String reportedBy = params.get("reportedBy").asText();
        final BusinessValue businessValue = BusinessValue.fromString(params.get("businessValue")
                                                                        .asText());
        final CustomerDemand customerDemand = CustomerDemand.fromString(params.get("customerDemand")
                                                            .asText());

        return new FeatureRequestTicket.FeatureRequestTicketBuilder(
                ticketId,
                TicketType.FEATURE_REQUEST,
                title,
                businessPriority,
                expertiseArea,
                businessValue,
                customerDemand,
                createdAt
        )
        .reportedBy(reportedBy)
        .build();
    }

    private static UIFeedbackTicket createUIFeedbackTicket(final JsonNode params,
                                                           final int ticketId,
                                                           final BusinessPriority businessPriority,
                                                           final LocalDate createdAt) {
        final String title = params.get("title").asText();
        final ExpertiseArea expertiseArea = ExpertiseArea.fromString(params.get("expertiseArea")
                .asText());
        final String reportedBy = params.get("reportedBy").asText();
        final BusinessValue businessValue = BusinessValue.fromString(params.get("businessValue")
                                                        .asText());
        final int usabilityScore = Integer.parseInt(params.get("usabilityScore").asText());

        UIFeedbackTicket.UIFeedbackTicketBuilder builder = new UIFeedbackTicket
                .UIFeedbackTicketBuilder(
                    ticketId,
                    TicketType.UI_FEEDBACK,
                    title,
                    businessPriority,
                    expertiseArea,
                    businessValue,
                    usabilityScore,
                    createdAt
                ).reportedBy(reportedBy);

        if (params.has("description") && !params.get("description").isNull()) {
            builder.description(params.get("description").asText());
        }

        if (params.has("uiElementId") && !params.get("uiElementId").isNull()) {
            builder.buildUiElementId(params.get("uiElementId").asText());
        }

        if (params.has("screenshotUrl") && !params.get("screenshotUrl").isNull()) {
            builder.buildScreenshotUrl(params.get("screenshotUrl").asText());
        }

        if (params.has("suggestedFix") && !params.get("suggestedFix").isNull()) {
            builder.buildSuggestedFix(params.get("suggestedFix").asText());
        }

        return builder.build();
    }
}
