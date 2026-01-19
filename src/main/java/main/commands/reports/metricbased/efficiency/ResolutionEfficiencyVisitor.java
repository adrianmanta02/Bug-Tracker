package main.commands.reports.metricbased.efficiency;

import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.BugTicket;
import main.ticket.FeatureRequestTicket;
import main.ticket.UIFeedbackTicket;

import static main.common.SystemConstants.MAXIMUM_BUG_EFFICIENCY;
import static main.common.SystemConstants.ROUNDING_FACTOR;
import static main.common.SystemConstants.MAXIMUM_FEATURE_REQUEST_EFFICIENCY;
import static main.common.SystemConstants.MAXIMUM_UI_EFFICIENCY;
import static main.common.SystemConstants.NORMALIZATION_FACTOR;
import static main.common.SystemConstants.SEVERITY_COEFFICIENT;

public final class ResolutionEfficiencyVisitor implements TicketMetricVisitor {
    /**
     * @see TicketMetricVisitor#visit(BugTicket)
     */
    @Override
    public double visit(final BugTicket bugTicket) {
        double gross = (double) ((bugTicket.getBugFrequency().getFrequency()
                + bugTicket.getBugSeverity().getSeverityFactor()) * SEVERITY_COEFFICIENT)
                / bugTicket.getDaysToResolve();

        double efficiency = gross * NORMALIZATION_FACTOR / MAXIMUM_BUG_EFFICIENCY;
        return Math.round(efficiency * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(FeatureRequestTicket)
     */
    @Override
    public double visit(final FeatureRequestTicket featureRequestTicket) {
        double gross = (double) (featureRequestTicket.getBusinessValue()
                .getBusinessValue() + featureRequestTicket.getCustomerDemand().getCustomerDemand())
                / featureRequestTicket.getDaysToResolve();

        double efficiency = gross * NORMALIZATION_FACTOR / MAXIMUM_FEATURE_REQUEST_EFFICIENCY;
        return Math.round(efficiency * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(UIFeedbackTicket)
     */
    @Override
    public double visit(final UIFeedbackTicket uiTicket) {
        double gross = (double) (uiTicket.getUsabilityScore()
                + uiTicket.getBusinessValue().getBusinessValue())
                / uiTicket.getDaysToResolve();

        double efficiency = gross * NORMALIZATION_FACTOR / MAXIMUM_UI_EFFICIENCY;
        return Math.round(efficiency * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}
