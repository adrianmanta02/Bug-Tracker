package main.ticket.actions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDate;

/**
 *  main.ticket.actions.TicketAction is a class representing the objects stored inside
 *  a {@link main.ticket.Ticket}'s history. It is used whenever a ticket's state changes,
 *  a ticket is assigned to a developer, or it is dropped etc.
 */
@Getter
@JsonPropertyOrder({"milestone", "from", "to", "by", "timestamp", "action"})
public final class TicketAction {
    private final ActionType action;
    private final String by;
    private final LocalDate timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String milestone;

    public static class TicketActionBuilder {
        private final ActionType action;
        private final String by;
        private final LocalDate timestamp;

        private String from;
        private String to;
        private String milestone;

        public TicketActionBuilder(final ActionType actionType,
                                   final String by,
                                   final LocalDate timestamp) {
            this.action = actionType;
            this.by = by;
            this.timestamp = timestamp;
        }

        /**
         * Adds optional field 'from' to the TicketAction object
         * @param fromState String field
         * @return enhanced builder instance with field added.
         */
        public TicketActionBuilder from(final String fromState) {
            this.from = fromState;
            return this;
        }

        /**
         * Adds optional field 'to' to the TicketAction object
         * @param toState String field
         * @return enhanced builder instance with field added.
         */
        public TicketActionBuilder to(final String toState) {
            this.to = toState;
            return this;
        }

        /**
         * Adds optional field milestone to the TicketAction object
         * @param milestoneName String field
         * @return enhanced builder instance with field added.
         */
        public TicketActionBuilder milestone(final String milestoneName) {
            this.milestone = milestoneName;
            return this;
        }

        /**
         * Generic method for Builder Pattern. Used after setting all optional parameters
         * and wanting to create the final object.
         * @return instance of object built
         */
        public TicketAction build() {
            return new TicketAction(this);
        }
    }

    private TicketAction(final TicketActionBuilder ticketActionBuilder) {
        this.action = ticketActionBuilder.action;
        this.by = ticketActionBuilder.by;
        this.timestamp = ticketActionBuilder.timestamp;
        this.from = ticketActionBuilder.from;
        this.to = ticketActionBuilder.to;
        this.milestone = ticketActionBuilder.milestone;
    }
}
