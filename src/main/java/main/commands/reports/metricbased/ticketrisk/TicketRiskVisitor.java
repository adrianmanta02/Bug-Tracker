package main.commands.reports.metricbased.ticketrisk;

import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.BugTicket;
import main.ticket.FeatureRequestTicket;
import main.ticket.UIFeedbackTicket;

import static main.common.SystemConstants.ROUNDING_FACTOR;
import static main.common.SystemConstants.MAXIMUM_BUG_RISK;
import static main.common.SystemConstants.NORMALIZATION_FACTOR;
import static main.common.SystemConstants.MAXIMUM_FEATURE_RISK;
import static main.common.SystemConstants.MAXIMUM_UI_USABILITY_SCORE;

public final class TicketRiskVisitor implements TicketMetricVisitor {
    /**
     * @see TicketMetricVisitor#visit(BugTicket)
     */
    @Override
    public double visit(final BugTicket bugTicket) {
        double gross = bugTicket.getBugFrequency().getFrequency()
                * bugTicket.getBugSeverity().getSeverityFactor();
        double value = gross * ROUNDING_FACTOR / MAXIMUM_BUG_RISK;
        return Math.round(value * NORMALIZATION_FACTOR) / NORMALIZATION_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(FeatureRequestTicket)
     */
    @Override
    public double visit(final FeatureRequestTicket featureRequestTicket) {
        double gross = featureRequestTicket.getBusinessValue().getBusinessValue()
                + featureRequestTicket.getCustomerDemand().getCustomerDemand();
        double value = gross * ROUNDING_FACTOR / MAXIMUM_FEATURE_RISK;
        return Math.round(value * NORMALIZATION_FACTOR) / NORMALIZATION_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(UIFeedbackTicket)
     */
    @Override
    public double visit(final UIFeedbackTicket uiTicket) {
        double gross = (MAXIMUM_UI_USABILITY_SCORE - uiTicket.getUsabilityScore())
                * uiTicket.getBusinessValue().getBusinessValue();
        return Math.round(gross * NORMALIZATION_FACTOR) / NORMALIZATION_FACTOR;
    }
}
