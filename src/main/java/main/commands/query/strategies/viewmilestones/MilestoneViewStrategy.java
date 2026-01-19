package main.commands.query.strategies.viewmilestones;

import main.commands.context.CommandContext;
import main.commands.query.strategies.ViewStrategy;
import main.milestone.Milestone;
import main.users.User;

import java.util.Comparator;
import java.util.List;

/**
 * Defines which rule to select when it comes to the execution of
 * {@link main.commands.query.ViewMilestonesCommand}
 * The rule depends on the {@link User}'s role.
 */
public interface MilestoneViewStrategy extends ViewStrategy<Milestone> {
    /**
     * Gets the Milestones needed for {@link main.commands.query.ViewMilestonesCommand},
     * following rules given by User's role. Each user have a different perspective
     * on the command's result.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @param user User instance whose role is deciding the algorithm for viewing
     * @return list of filtered Milestones
     */
    List<Milestone> getItems(CommandContext commandContext, User user);


    /**
     * Orders the filtered milestones by due date, then by name.
     * @param items the filtered list of milestones to sort
     * @return an unmodifiable list of milestones, sorted by due date and name
     */
    @Override
    default List<Milestone> sortResult(final List<Milestone> items) {
        return items.stream()
                .sorted(Comparator.comparing(Milestone::getDueDate)
                        .thenComparing(Milestone::getName))
                .toList();
    }
}
