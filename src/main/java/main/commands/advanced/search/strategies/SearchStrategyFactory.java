package main.commands.advanced.search.strategies;

import main.users.User;

public final class SearchStrategyFactory {
    private SearchStrategyFactory() { }
    /**
     * Creates and returns a search strategy based on the user's role and the target
     * object to search. The concrete {@link SearchStrategy} implementation is
     * selected according to the role of the given user. For managers, the strategy
     * further depends on the type of object to be searched.
     * @param user the user requesting the search operation
     * @param objectToSearch the type of object to search
     *                       (e.g. {@code "TICKET"}, {@code "DEVELOPER"})
     * @return a {@link SearchStrategy} appropriate for the user's role and
     * search target
     * @throws IllegalArgumentException if the user role or the search object type
     * is not supported
     */
    public static SearchStrategy<?> createStrategy(final User user,
                                                   final String objectToSearch) {
        return switch (user.getRole()) {
            case DEVELOPER -> new DeveloperSearchStrategy();
            case MANAGER -> switch (objectToSearch) {
                case "TICKET" -> new ManagerTicketSearchStrategy();
                case "DEVELOPER" -> new ManagerDevelopersSearchStrategy();
                default -> throw new IllegalArgumentException("Unknown search parameter: "
                        + objectToSearch);
            };

            default -> throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        };
    }
}
