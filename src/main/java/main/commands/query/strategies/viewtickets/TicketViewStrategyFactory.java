package main.commands.query.strategies.viewtickets;
import main.users.User;

public final class TicketViewStrategyFactory {
    private TicketViewStrategyFactory() { }

    /**
     * Creates a ticket view strategy, used for {@link TicketViewStrategy} command's
     * execution. Each user has different access to the ticket, based on their
     * assigned role.
     * @param user User instance, which role decides the creation of the strategy
     * @return a {@link TicketViewStrategy}
     */
    public static TicketViewStrategy createCommandStrategy(final User user) {
        return switch (user.getRole()) {
            case REPORTER -> new ReporterTicketViewStrategy();
            case MANAGER -> new ManagerTicketViewStrategy();
            case DEVELOPER -> new DeveloperTicketViewStrategy();
        };
    }
}
