package main.commands.reports.metricbased;

import main.ticket.BugTicket;
import main.ticket.FeatureRequestTicket;
import main.ticket.UIFeedbackTicket;

public interface TicketMetricVisitor {
    /**
     * Visits {@link BugTicket} in order to retrieve values from its fields
     * and do calculations following a formula, in order to help generating
     * multiple reports.
     * @param bugTicket ticket from which the Visitor retrieves data.
     * @return the normalized result of the calculation being done
     */
    double visit(BugTicket bugTicket);

    /**
     * Visits {@link FeatureRequestTicket} in order to retrieve values from its fields
     * and do calculations following a formula, in order to help generating
     * multiple reports.
     * @param featureRequestTicket ticket from which the Visitor retrieves data.
     * @return the normalized result of the calculation being done
     */
    double visit(FeatureRequestTicket featureRequestTicket);

    /**
     * Visits {@link UIFeedbackTicket} in order to retrieve values from its fields
     * and do calculations following a formula, in order to help generating
     * multiple reports.
     * @param uiFeedbackTicket ticket from which the Visitor retrieves data.
     * @return the normalized result of the calculation being done
     */
    double visit(UIFeedbackTicket uiFeedbackTicket);
}
