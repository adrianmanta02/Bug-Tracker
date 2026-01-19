package main.commands.query.strategies.viewmilestones;

import main.users.User;

public final class MilestoneViewStrategyFactory {
    private MilestoneViewStrategyFactory() { }

    /**
     * Creates a {@link main.milestone.Milestone} view strategy, which is different
     * for users, according to their {@link main.users.enums.common.Role}.
     * @param user User instance the factory takes it's creating decision to
     * @return a {@code MilestoneViewStrategy}
     */
    public static MilestoneViewStrategy createCommandStrategy(final User user) {
        return switch (user.getRole()) {
            case MANAGER -> new ManagerMilestoneViewStrategy();
            case DEVELOPER -> new DeveloperMilestoneViewStrategy();
            case REPORTER -> null;
        };
    }
}
