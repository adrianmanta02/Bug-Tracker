package main.commands.advanced.search.strategies;

import main.commands.advanced.search.filters.utils.SearchContext;
import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.filters.utils.TicketFilterBuilder;
import main.ticket.Ticket;

import java.util.Comparator;
import java.util.List;

/**
 * Defines the searching algorithm applied to retrieve information about {@link Ticket}s
 * or other {@link main.users.Developer}s. The selection of the algorithm depends on
 * invoker's assigned {@link main.users.enums.common.Role}.
 * @param <T> the result's form wrapped in a DTO for the custom display of the item
 *           searched
 */
public interface SearchStrategy<T> {
    /**
     * Wrapper method to do the searching process, which does a query on a certain Container,
     * depending on the item searched.
     *
     * @param filter filters to be applied during the search
     * @param searchContext instance providing additional information for certain search
     *  *                needs
     * @return a list of items obtained after applying the {@code filter}
     */
    List<T> search(SearchFilter filter, SearchContext searchContext);

    /**
     * Common method to add the filters over all current tickets, being applied for
     * multiple searching strategies.
     * @param filter the filters added over the list of existing {@link Ticket}s
     * @param tickets the group of Tickets to be filtered
     * @param context instance providing additional information for certain search
     *                needs
     * @return a list of filtered Tickets, ordered by their creation time and
     *              unique ID
     */
    default List<Ticket> applyFilters(final SearchFilter filter,
                                      final List<Ticket> tickets,
                                      final SearchContext context) {
        TicketFilterBuilder filterBuilder = new TicketFilterBuilder();
        final List<SearchFilterStrategy<Ticket>> filters = filterBuilder.build(filter);

        return tickets.stream()
                .filter(ticketToApplyFilter -> filters.stream()
                        .allMatch(filterToApply ->
                                filterToApply.matches(ticketToApplyFilter, filter, context)))
                .sorted(Comparator.comparing(Ticket::getCreatedAt)
                        .thenComparing(Ticket::getId))
                .toList();
    }
}
