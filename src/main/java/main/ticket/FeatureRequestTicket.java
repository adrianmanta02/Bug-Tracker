package main.ticket;

import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.feature.BusinessValue;
import main.ticket.enums.feature.CustomerDemand;

public class FeatureRequestTicket extends Ticket {
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    public static class FeatureRequestTicketBuilder extends
            TicketBuilder<FeatureRequestTicket, FeatureRequestTicketBuilder> {
        private BusinessValue businessValue;
        private CustomerDemand customerDemand;

        public FeatureRequestTicketBuilder(final int id, final String type,
                                           final String title,
                                           final BusinessPriority businessPriority,
                                           final TicketStatus status,
                                           final ExpertiseArea expertiseArea,
                                           final BusinessValue businessValue,
                                           final CustomerDemand customerDemand) {
            super(id, type, title, businessPriority, status, expertiseArea);
            this.customerDemand = customerDemand;
            this.businessValue = businessValue;
        }

        @Override
        public FeatureRequestTicketBuilder self() {
            return this;
        }

        @Override
        public FeatureRequestTicket build() {
            return new FeatureRequestTicket(this);
        }
    }

    private FeatureRequestTicket(final FeatureRequestTicketBuilder builder) {
        super(builder);
        this.customerDemand = builder.customerDemand;
        this.businessValue = builder.businessValue;
    }
}
