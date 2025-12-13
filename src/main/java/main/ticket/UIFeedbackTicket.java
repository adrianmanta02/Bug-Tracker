package main.ticket;

import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.feature.BusinessValue;

import java.rmi.server.UID;

public class UIFeedbackTicket extends Ticket {
    private String uiElementId;
    private BusinessValue businessValue;
    private int usabilityScore; // 1-10
    private String screenshotUrl;
    private String suggestedFix;

    public static class UIFeedbackTicketBuilder extends
            TicketBuilder<UIFeedbackTicket, UIFeedbackTicketBuilder> {
        private String uiElementId;
        private BusinessValue businessValue;
        private int usabilityScore; // 1-10
        private String screenshotUrl;
        private String suggestedFix;

        public UIFeedbackTicketBuilder(final int id, final String type, final String title,
                                        final BusinessPriority businessPriority, final TicketStatus status,
                                        final ExpertiseArea expertiseArea,
                                        final BusinessValue businessValue,
                                        final int usabilityScore) {
            super(id, type, title, businessPriority, status, expertiseArea);
            this.businessValue = businessValue;
            this.usabilityScore = usabilityScore;
        }

        public UIFeedbackTicketBuilder uiElementId(final String uiElementId) {
            this.uiElementId = uiElementId;
            return this;
        }

        public UIFeedbackTicketBuilder screenshotUrl(final String screenshotUrl) {
            this.screenshotUrl = screenshotUrl;
            return this;
        }

        public UIFeedbackTicketBuilder suggestedFix(final String suggestedFix) {
            this.suggestedFix = suggestedFix;
            return this;
        }

        @Override
        public UIFeedbackTicketBuilder self() {
            return this;
        }

        @Override
        public UIFeedbackTicket build() {
            return new UIFeedbackTicket(this);
        }
    }

    private UIFeedbackTicket (UIFeedbackTicketBuilder builder) {
        super(builder);
        this.uiElementId = builder.uiElementId;
        this.businessValue = builder.businessValue;
        this.usabilityScore = builder.usabilityScore;
        this.screenshotUrl = builder.screenshotUrl;
        this.suggestedFix = builder.suggestedFix;
    }
}
