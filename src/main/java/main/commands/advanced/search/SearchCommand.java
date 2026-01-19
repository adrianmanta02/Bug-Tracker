package main.commands.advanced.search;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.strategies.SearchStrategy;
import main.commands.advanced.search.strategies.SearchStrategyFactory;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.users.enums.common.Role;
import static main.common.SystemConstants.FILTERS_FIELD;
import static main.common.SystemConstants.SEARCH_TYPE_FIELD;
import static main.common.SystemConstants.RESULTS_FIELD;

import java.util.List;

public final class SearchCommand extends Command {
    private final JsonNode filtersNode;
    public SearchCommand(final JsonNode node) {
        super(node);
        this.filtersNode = node.get(FILTERS_FIELD);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.MANAGER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    /**
     * Executes the searching algorithm, which is dependent on
     * invoker's {@link Role}. The searching can occur on both Ticket and
     * Developer groups, being given a set of filters by the one calling
     * the command.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a list of ordered and filtered results.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        SearchContext searchContext = new SearchContext(getInvoker(), getTimestamp());
        SearchStrategy<?> searchStrategy = SearchStrategyFactory
                .createStrategy(this.getInvoker(), filtersNode.get(SEARCH_TYPE_FIELD).asText());

        SearchFilter searchFilter = SearchFilter.fromJsonNode(filtersNode);
        List<?> results = searchStrategy.search(searchFilter, searchContext);

        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        )
        .data(SEARCH_TYPE_FIELD, searchFilter.getSearchType())
        .data(RESULTS_FIELD, results)
        .build();
    }
}
