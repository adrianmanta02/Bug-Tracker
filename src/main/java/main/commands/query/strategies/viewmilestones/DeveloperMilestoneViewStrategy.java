package main.commands.query.strategies.viewmilestones;

import main.commands.context.CommandContext;
import main.milestone.Milestone;
import main.users.User;

import java.util.List;

public final class DeveloperMilestoneViewStrategy
        implements MilestoneViewStrategy {
    /**
     * @see MilestoneViewStrategy#getItems(CommandContext, User)
     */
    @Override
    public List<Milestone> getItems(final CommandContext commandContext, final User user) {
        return sortResult(commandContext
                .getMilestoneContainer()
                .getMilestonesAssociatedTo(user.getUsername())
        );
    }
}
