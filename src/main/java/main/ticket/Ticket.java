package main.ticket;

import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.common.TicketType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Ticket {
    private int id;
    private TicketType type;
    private String title;
    private BusinessPriority businessPriority;
    private TicketStatus status;
    private ExpertiseArea expertiseArea;

    private String description;
    private String reportedBy;

    public static abstract class TicketBuilder<T extends Ticket, B extends TicketBuilder<T,B>> {
        private int id;
        private TicketType type;
        private String title;
        private BusinessPriority businessPriority;
        private TicketStatus status;
        private ExpertiseArea expertiseArea;

        private String description;
        private String reportedBy;

        public TicketBuilder(final int id, final TicketType type, final String title,
                             final BusinessPriority businessPriority, final TicketStatus status,
                             final ExpertiseArea expertiseArea) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.businessPriority = businessPriority;
            this.status = status;
            this.expertiseArea = expertiseArea;
        }

        public B description(final String description) {
            this.description = description;
            return self();
        }

        public B reportedBy(final String reportedBy) {
            this.reportedBy = reportedBy;
            return self();
        }

        protected abstract B self();
        protected abstract T build();
    }

    public Ticket(TicketBuilder<?, ?> ticketBuilder) {
        this.id = ticketBuilder.id;
        this.type = ticketBuilder.type;
        this.title = ticketBuilder.title;
        this.businessPriority = ticketBuilder.businessPriority;
        this.status = ticketBuilder.status;
        this.expertiseArea = ticketBuilder.expertiseArea;
        this.description = ticketBuilder.description;
        this.reportedBy = ticketBuilder.reportedBy;
    }
}
