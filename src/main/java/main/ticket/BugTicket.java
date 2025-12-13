package main.ticket;

import main.ticket.enums.bug.BugFrequency;
import main.ticket.enums.bug.BugSeverity;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketStatus;

public class BugTicket extends Ticket {
    private String expectedBehavior;
    private String actualBehavior;
    private BugFrequency bugFrequency;
    private BugSeverity bugSeverity;

    private String environment;
    private int errorCode;

    public static class BugTicketBuilder extends TicketBuilder<BugTicket, BugTicketBuilder>{
        private String expectedBehavior;
        private String actualBehavior;
        private BugFrequency bugFrequency;
        private BugSeverity bugSeverity;

        private String environment;
        private int errorCode;

        public BugTicketBuilder(final int id, final String type, final String title,
                                final BusinessPriority businessPriority, final TicketStatus status,
                                final ExpertiseArea expertiseArea, final BugFrequency bugFrequency,
                                final BugSeverity bugSeverity) {
            super(id, type, title, businessPriority, status, expertiseArea);
            this.bugFrequency = bugFrequency;
            this.bugSeverity = bugSeverity;
        }

        public BugTicketBuilder environment(final String environment) {
            this.environment = environment;
            return this;
        }

        public BugTicketBuilder errorCode(final int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        @Override
        public BugTicketBuilder self() {
            return this;
        }

        @Override
        public BugTicket build() {
            return new BugTicket(this);
        }
    }

    private BugTicket(BugTicketBuilder bugBuilder) {
        super(bugBuilder);
        this.expectedBehavior = bugBuilder.expectedBehavior;
        this.actualBehavior = bugBuilder.actualBehavior;
        this.bugFrequency = bugBuilder.bugFrequency;
        this.bugSeverity = bugBuilder.bugSeverity;
        this.errorCode = bugBuilder.errorCode;
        this.environment = bugBuilder.environment;
    }
}
