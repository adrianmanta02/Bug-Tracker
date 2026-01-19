package main.commands;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.advanced.notifications.ViewNotificationsCommand;
import main.commands.advanced.search.SearchCommand;
import main.commands.commentcommands.AddCommentCommand;
import main.commands.commentcommands.UndoAddCommentCommand;
import main.commands.flow.LostInvestorsCommand;
import main.commands.flow.StartTestingPhaseCommand;
import main.commands.milestonecommands.CreateMilestoneCommand;
import main.commands.query.ViewAssignedTicketsCommand;
import main.commands.query.ViewMilestonesCommand;
import main.commands.query.ViewTicketsCommand;
import main.commands.reports.metricbased.customerimpact.GenerateCustomerImpactReportCommand;
import main.commands.reports.metricbased.efficiency.GenerateResolutionEfficiencyReportCommand;
import main.commands.reports.metricbased.stability.AppStabilityReportCommand;
import main.commands.reports.metricbased.ticketrisk.GenerateTicketRiskReportCommand;
import main.commands.reports.performance.GeneratePerformanceReportCommand;
import main.commands.ticket.AssignTicketCommand;
import main.commands.ticket.ReportTicketCommand;
import main.commands.ticket.UndoAssignTicketCommand;
import main.commands.ticket.ChangeStatusCommand;
import main.commands.ticket.UndoChangeStatusCommand;
import main.commands.query.ViewTicketHistoryCommand;

import static main.common.SystemConstants.COMMAND_FIELD;

public final class CommandFactory {
    private static CommandFactory instance;
    private CommandFactory() { }

    /**
     * Returns the Singleton instance of the container.
     * The instance is lazily initialized on the first invocation of this
     * method.
     * @return the singleton {@code CommandFactory} instance
     */
    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    /**
     * Creates and returns a {@link Command} instance for further executions.
     * It is a Builder Pattern-specific method
     * @param jsonNode the JsonNode used for deserialization in order to get
     *                 properties used inside the commands.
     * @return an object that describes an executable Command, which is first
     * validated before running. The validation and executions differ between
     * commands.
     */
    public Command createCommand(final JsonNode jsonNode) {
        String commandType = jsonNode.get(COMMAND_FIELD).asText();

        return switch (commandType) {
            case "reportTicket" -> new ReportTicketCommand(jsonNode);
            case "viewTickets" -> new ViewTicketsCommand(jsonNode);
            case "createMilestone" -> new CreateMilestoneCommand(jsonNode);
            case "viewMilestones" -> new ViewMilestonesCommand(jsonNode);
            case "startTestingPhase" -> new StartTestingPhaseCommand(jsonNode);
            case "lostInvestors" -> new LostInvestorsCommand(jsonNode);
            case "assignTicket" -> new AssignTicketCommand(jsonNode);
            case "undoAssignTicket" -> new UndoAssignTicketCommand(jsonNode);
            case "viewAssignedTickets" -> new ViewAssignedTicketsCommand(jsonNode);
            case "addComment" -> new AddCommentCommand(jsonNode);
            case "undoAddComment" -> new UndoAddCommentCommand(jsonNode);
            case "changeStatus" -> new ChangeStatusCommand(jsonNode);
            case "undoChangeStatus" -> new UndoChangeStatusCommand(jsonNode);
            case "viewTicketHistory" -> new ViewTicketHistoryCommand(jsonNode);
            case "search" -> new SearchCommand(jsonNode);
            case "viewNotifications" -> new ViewNotificationsCommand(jsonNode);
            case "generateCustomerImpactReport" ->
                    new GenerateCustomerImpactReportCommand(jsonNode);
            case "generateTicketRiskReport" -> new GenerateTicketRiskReportCommand(jsonNode);
            case "generateResolutionEfficiencyReport" ->
                    new GenerateResolutionEfficiencyReportCommand(jsonNode);
            case "appStabilityReport" -> new AppStabilityReportCommand(jsonNode);
            case "generatePerformanceReport" -> new GeneratePerformanceReportCommand(jsonNode);

            default -> throw new IllegalArgumentException("Unknown command: " + commandType);
        };
    }
}
