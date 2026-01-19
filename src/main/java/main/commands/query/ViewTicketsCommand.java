package main.commands.query;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.query.strategies.viewtickets.TicketViewStrategy;
import main.commands.query.strategies.viewtickets.TicketViewStrategyFactory;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.TICKETS_FIELD;

public final class ViewTicketsCommand extends Command {
    public ViewTicketsCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER, Role.REPORTER, Role.DEVELOPER);
    }
    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        TicketViewStrategy commandExecuteStrategy =
                TicketViewStrategyFactory.createCommandStrategy(this.getInvoker());
        List<Ticket> ticketsToView = commandExecuteStrategy.getItems(commandContext,
                                                                    this.getInvoker());
        return new CommandResult.CommandResultBuilder(
            this.command,
            this.username,
            this.timestamp
        ).data(TICKETS_FIELD, ticketsToView)
        .build();
    }


    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }
}
