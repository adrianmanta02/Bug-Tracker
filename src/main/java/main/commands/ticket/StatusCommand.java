package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.commands.strategies.status.TicketStatusValidation;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.util.List;

/**
 * main.commands.ticket.StatusCommand is a class responsible with
 * encapsulating the properties of a changing status command, as by
 * {@link ChangeStatusCommand} and {@link UndoChangeStatusCommand}.
 */
@Getter
public abstract class StatusCommand extends Command {
    protected final int ticketId;
    protected Ticket ticketToChange;

    public StatusCommand(final JsonNode node) {
        super(node);
        this.ticketId = node.get("ticketID").asInt();
        ticketToChange = TicketContainer.getInstance().getTicketById(ticketId);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        final TicketStatusValidation statusValidation = new TicketStatusValidation();
        return statusValidation.validate(commandContext, this);
    }

    /**
     * Implements the execution for status-related commands,
     * like {@link ChangeStatusCommand} or {@link UndoChangeStatusCommand}.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of handling the command
     */
    public abstract CommandResult execute(CommandContext commandContext);
}
