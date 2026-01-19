package main.commands.commentcommands;

import main.commands.CommandValidationStrategy;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.users.User;

/*
    main.commands.strategies.comment.DeveloperAddCommentValidation is a strategy
    designed for validating the execution of AddCommentCommand. It provides an
    extra layer of validation, apart from the common validation rules already
    defined by the command itself.
 */
public final class DeveloperAddCommentValidation
        implements CommandValidationStrategy<AddCommentCommand> {

    /**
     * @see CommandValidationStrategy#validate(CommandContext, main.commands.Command)
     */
    @Override
    public CommandResult validate(final CommandContext commandContext,
                                  final AddCommentCommand command) {
        Ticket ticket = command.getTicket();
        User user = command.getInvoker();

        if (!user.canCommentOn(ticket)) {
            return command.buildErrorResult(
                    "Ticket " + ticket.getId()
                            + " is not assigned to the developer "
                            + user.getUsername()
                            + "."
            );
        }
        return null;
    }
}
