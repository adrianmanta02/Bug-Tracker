package main.commands.query;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.commands.query.strategies.ViewStrategy;
import main.commands.query.strategies.viewhistory.ViewHistoryStrategyFactory;
import main.commands.ticket.TicketHistoryDTO;
import main.users.enums.common.Role;

import java.util.List;

public final class ViewTicketHistoryCommand extends Command {
    public ViewTicketHistoryCommand(final JsonNode jsonNode) {
        super(jsonNode);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.MANAGER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    /**
     * Retrieves the history for multiple {@link main.ticket.Ticket}. The history
     * consists of multiple actions made on a ticket, including state changes
     * given by being solved or unassigned etc.
     * Each user has a different perspective on Ticket history, depending on
     * their role.
     * @param commandContext the context in which the command is executed
     *      *                providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of executing the command
     */
    public CommandResult execute(final CommandContext commandContext) {
        ViewStrategy<TicketHistoryDTO> viewHistoryStrategy = ViewHistoryStrategyFactory
                .createStrategy(this.getInvoker());
        List<TicketHistoryDTO> ticketsHistories = viewHistoryStrategy
                .getItems(commandContext, this.getInvoker());

        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        ).data("ticketHistory", ticketsHistories)
        .build();
    }
}
