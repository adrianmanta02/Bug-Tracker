package main.ticket;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketType;
import main.ticket.enums.feature.BusinessValue;

@Getter
public final class UIFeedbackTicket extends Ticket {
    @JsonIgnore
    private String uiElementId;
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private int usabilityScore; // 1-10
    @JsonIgnore
    private String screenshotUrl;
    @JsonIgnore
    private String suggestedFix;

    public static class UIFeedbackTicketBuilder extends
        TicketBuilder<UIFeedbackTicket, UIFeedbackTicketBuilder> {
        private String uiElementId;
        private BusinessValue businessValue;
        private int usabilityScore;
        private String screenshotUrl;
        private String suggestedFix;

        public UIFeedbackTicketBuilder(final int id, final TicketType type, final String title,
                                       final BusinessPriority businessPriority,
                                       final ExpertiseArea expertiseArea,
                                       final BusinessValue businessValue,
                                       final int usabilityScore,
                                       final LocalDate createdAt) {
            super(id, type, title, businessPriority, expertiseArea, createdAt);
            this.businessValue = businessValue;
            this.usabilityScore = usabilityScore;
        }

        /**
         * Adds optional field uiElementId
         * @param uiElement String field
         * @return enhanced builder instance with field added.
         */
        public UIFeedbackTicketBuilder buildUiElementId(final String uiElement) {
            this.uiElementId = uiElement;
            return this;
        }

        /**
         * Adds optional field screenshotUrl
         * @param url String field
         * @return enhanced builder instance with field added.
         */
        public UIFeedbackTicketBuilder buildScreenshotUrl(final String url) {
            this.screenshotUrl = url;
            return this;
        }

        /**
         * Adds optional field suggestedFix
         * @param fix String field
         * @return enhance builder instance with field added.
         */
        public UIFeedbackTicketBuilder buildSuggestedFix(final String fix) {
            this.suggestedFix = fix;
            return this;
        }

        /**
         * @see TicketBuilder#self()
         */
        @Override
        public UIFeedbackTicketBuilder self() {
            return this;
        }

        /**
         * @see Ticket.TicketBuilder#build()
         */
        @Override
        public UIFeedbackTicket build() {
            return new UIFeedbackTicket(this);
        }
    }

    private UIFeedbackTicket(final UIFeedbackTicketBuilder builder) {
        super(builder);
        this.uiElementId = builder.uiElementId;
        this.businessValue = builder.businessValue;
        this.usabilityScore = builder.usabilityScore;
        this.screenshotUrl = builder.screenshotUrl;
        this.suggestedFix = builder.suggestedFix;
    }

    @Override
    public double accept(final TicketMetricVisitor visitor) {
        return visitor.visit(this);
    }
}
