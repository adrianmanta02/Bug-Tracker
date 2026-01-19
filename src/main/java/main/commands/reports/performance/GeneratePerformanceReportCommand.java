package main.commands.reports.performance;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.commands.reports.performance.utils.dtos.PerformanceReportDTO;
import main.commands.reports.performance.utils.services.PerformanceReportService;
import main.users.Manager;
import main.users.UserContainer;
import main.users.enums.common.Role;

import java.time.Month;
import java.util.List;

public final class GeneratePerformanceReportCommand extends Command {
    public GeneratePerformanceReportCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    /**
     * Executes the command, calculating the performance report for every single
     * {@link main.users.Developer} that is a Manager-invoker's subordinate.
     * The calculation depends on statistics being made a month earlier than the
     * moment in which the command is being invoked. All metrics are related
     * to Developer's performance during a month, helping the Manager keeping
     * track of the issues solved and the quality of service.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a report consisting of multiple Developer self-statistic
     * centralized analysis.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        Manager manager = UserContainer.getInstance().getManagerByUsername(this.username);
        final Month month = this.timestamp.minusMonths(1).getMonth();

        PerformanceReportService performanceReportService = new PerformanceReportService(
                UserContainer.getInstance(),
                commandContext.getTicketContainer()
        );

        List<PerformanceReportDTO> reports = performanceReportService
                .generateForManager(manager, month);
        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        ).data("report", reports)
        .build();
    }
}
