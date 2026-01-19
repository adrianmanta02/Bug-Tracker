package main.commands.query.strategies.viewhistory;

import main.commands.context.CommandContext;
import main.commands.query.strategies.ViewStrategy;
import main.commands.ticket.TicketHistoryDTO;
import main.ticket.utils.HistoryManager;
import main.users.User;

import java.util.Comparator;
import java.util.List;

public final class DeveloperViewHistoryStrategy
        implements ViewStrategy<TicketHistoryDTO> {
    /**
     * @see ViewStrategy#getItems(CommandContext, User)
     */
    @Override
    public List<TicketHistoryDTO> getItems(final CommandContext commandContext,
                                           final User user) {

        TicketHistoryService historyService =
                new TicketHistoryService(HistoryManager.getInstance(),
                                        commandContext.getTicketContainer(),
                                        commandContext.getMilestoneContainer());
        return sortResult(historyService.getDeveloperTicketHistory(user.getUsername()));
    }

    /**
     * @see ViewStrategy#sortResult(List)
     */
    @Override
    public List<TicketHistoryDTO> sortResult(final List<TicketHistoryDTO> items) {
        return items
                .stream()
                .sorted(
                        Comparator.comparing(TicketHistoryDTO::getCreatedAt)
                                .thenComparing(TicketHistoryDTO::getId)
                )
                .toList();
    }
}
