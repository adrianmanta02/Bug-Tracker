package main.commands.query.strategies.viewhistory;

import main.commands.query.strategies.ViewStrategy;
import main.commands.ticket.TicketHistoryDTO;
import main.users.User;

public final class ViewHistoryStrategyFactory {
    private ViewHistoryStrategyFactory() { }

    /**
     * Creates and returns a view strategy for ticket history based on the user's role.
     * The concrete {@link ViewStrategy} implementation is selected according to the
     * role of the given user.
     * @param user the user for whom the view strategy is to be created
     * @return a {@link ViewStrategy} suitable for the user's role,
     *         or {@code null} if the role does not support viewing ticket history
     */
    public static ViewStrategy<TicketHistoryDTO> createStrategy(final User user) {
        return switch (user.getRole()) {
            case DEVELOPER -> new DeveloperViewHistoryStrategy();
            case MANAGER -> new ManagerViewHistoryStrategy();
            case REPORTER -> null;
        };
    }
}
