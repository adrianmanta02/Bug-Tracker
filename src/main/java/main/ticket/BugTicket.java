package main.ticket;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.enums.bug.BugFrequency;
import main.ticket.enums.bug.BugSeverity;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketType;

@Getter
public final class BugTicket extends Ticket {
    @JsonIgnore
    private String expectedBehavior;
    @JsonIgnore
    private String actualBehavior;
    @JsonIgnore
    private BugFrequency bugFrequency;
    @JsonIgnore
    private BugSeverity bugSeverity;

    @JsonIgnore
    private String environment;
    @JsonIgnore
    private int errorCode;


    public static class BugTicketBuilder extends TicketBuilder<BugTicket, BugTicketBuilder> {
        @JsonIgnore
        private String expectedBehavior;
        @JsonIgnore
        private String actualBehavior;
        @JsonIgnore
        private BugFrequency bugFrequency;
        @JsonIgnore
        private BugSeverity bugSeverity;

        @JsonIgnore
        private String environment;
        @JsonIgnore
        private int errorCode;

        public BugTicketBuilder(final int id, final TicketType type, final String title,
                                final BusinessPriority businessPriority,
                                final ExpertiseArea expertiseArea, final String expectedBehavior,
                                final String actualBehavior, final BugFrequency bugFrequency,
                                final BugSeverity bugSeverity,
                                final LocalDate createdAt) {
            super(id, type, title, businessPriority, expertiseArea, createdAt);
            this.expectedBehavior = expectedBehavior;
            this.actualBehavior = actualBehavior;
            this.bugFrequency = bugFrequency;
            this.bugSeverity = bugSeverity;
        }

        /**
         * Adds optional field environment to the BugTicket object
         * @param environmentName String field
         * @return enhanced builder instance with field added.
         */
        public BugTicketBuilder environment(final String environmentName) {
            this.environment = environmentName;
            return this;
        }

        /**
         * Adds optional field errorCode to the BugTicket object
         * @param code String field
         * @return enhanced builder instance with field added.
         */
        public BugTicketBuilder errorCode(final int code) {
            this.errorCode = code;
            return this;
        }

        /**
         * @see TicketBuilder#self()
         */
        @Override
        public BugTicketBuilder self() {
            return this;
        }

        /**
         * @see TicketBuilder#build()
         */
        @Override
        public BugTicket build() {
            return new BugTicket(this);
        }
    }

    private BugTicket(final BugTicketBuilder bugBuilder) {
        super(bugBuilder);
        this.expectedBehavior = bugBuilder.expectedBehavior;
        this.actualBehavior = bugBuilder.actualBehavior;
        this.bugFrequency = bugBuilder.bugFrequency;
        this.bugSeverity = bugBuilder.bugSeverity;
        this.errorCode = bugBuilder.errorCode;
        this.environment = bugBuilder.environment;
    }

    @Override
    public double accept(final TicketMetricVisitor visitor) {
        return visitor.visit(this);
    }
}
