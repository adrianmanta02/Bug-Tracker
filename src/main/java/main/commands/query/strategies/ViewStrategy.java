package main.commands.query.strategies;

import main.commands.context.CommandContext;
import main.users.User;

import java.util.List;

public interface ViewStrategy<T> {
    /**
     * Filters the objects based on the rule chosen.
     * @param commandContext instance containing useful data for running a command
     * @param user User instance whose role is deciding the algorithm for viewing
     * @return an ordered List of objects
     */
    List<T> getItems(CommandContext commandContext, User user);

    /**
     * Sorts the list of objects.
     * @param items the objects obtained after filtering
     * @return the ordered list of objects
     */
    List<T> sortResult(List<T> items);
}
