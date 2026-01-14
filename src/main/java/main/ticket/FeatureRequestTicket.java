package main.ticket;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketType;
import main.ticket.enums.feature.BusinessValue;
import main.ticket.enums.feature.CustomerDemand;

@Getter
public final class FeatureRequestTicket extends Ticket {
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private CustomerDemand customerDemand;

    public final static class FeatureRequestTicketBuilder extends
            TicketBuilder<FeatureRequestTicket, FeatureRequestTicketBuilder> {
        private BusinessValue businessValue;
        private CustomerDemand customerDemand;

        public FeatureRequestTicketBuilder(final int id, final TicketType type,
                                           final String title,
                                           final BusinessPriority businessPriority,
                                           final ExpertiseArea expertiseArea,
                                           final BusinessValue businessValue,
                                           final CustomerDemand customerDemand,
                                           final LocalDate createdAt) {
            super(id, type, title, businessPriority, expertiseArea, createdAt);
            this.customerDemand = customerDemand;
            this.businessValue = businessValue;
        }

        /**
         * @see TicketBuilder#self()
         */
        @Override
        public FeatureRequestTicketBuilder self() {
            return this;
        }

        /**
         * @see TicketBuilder#build()
         */
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

    @Override
    public double accept(final TicketMetricVisitor visitor) {
        return visitor.visit(this);
    }
}
