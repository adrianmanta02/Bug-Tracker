package main.commands.reports.metricbased.customerimpact;

import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.BugTicket;
import main.ticket.FeatureRequestTicket;
import main.ticket.UIFeedbackTicket;

import static main.common.SystemConstants.NORMALIZATION_FACTOR;
import static main.common.SystemConstants.ROUNDING_FACTOR;
import static main.common.SystemConstants.MAXIMUM_BUG_IMPACT;

/**
 * CustomerImpactVisitor is a class implementing the visitor pattern, used to retrieve
 * and calculate multiple coefficients used in generating the app's report regarding
 * its impact over customers.
 */
public final class CustomerImpactVisitor implements TicketMetricVisitor {
    /**
     * @see TicketMetricVisitor#visit(BugTicket)
     */
    @Override
    public double visit(final BugTicket bugTicket) {
        int gross = bugTicket.getBusinessPriority().getPriority()
                * bugTicket.getBugSeverity().getSeverityFactor()
                * bugTicket.getBugFrequency().getFrequency();
        double value = (double) gross * NORMALIZATION_FACTOR / MAXIMUM_BUG_IMPACT;
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(UIFeedbackTicket)
     */
    @Override
    public double visit(final UIFeedbackTicket uiFeedbackTicket) {
        int gross = uiFeedbackTicket.getBusinessValue().getBusinessValue()
                * uiFeedbackTicket.getUsabilityScore();
        double value = (double) gross * NORMALIZATION_FACTOR / NORMALIZATION_FACTOR;
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * @see TicketMetricVisitor#visit(FeatureRequestTicket)
     */
    @Override
    public double visit(final FeatureRequestTicket featureRequestTicket) {
        int gross = featureRequestTicket.getBusinessValue().getBusinessValue()
                * featureRequestTicket.getCustomerDemand().getCustomerDemand();

        double value = (double) gross * NORMALIZATION_FACTOR / NORMALIZATION_FACTOR;
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}
