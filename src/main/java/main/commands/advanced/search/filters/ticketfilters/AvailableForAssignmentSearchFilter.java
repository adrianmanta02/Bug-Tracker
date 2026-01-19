package main.commands.advanced.search.filters.ticketfilters;

import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.strategies.SearchFilterStrategy;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.ticket.Ticket;

public final class AvailableForAssignmentSearchFilter implements SearchFilterStrategy<Ticket> {
    /**
     * @see SearchFilterStrategy#matches(Object, SearchFilter, SearchContext)
     */
    public boolean matches(final Ticket ticket, final SearchFilter filter,
                           final SearchContext context) {
        return !Boolean.TRUE.equals(filter.getAvailableForAssignment())
                || (context.getUser().canAccess(ticket) && ticket.getAssignedTo().isEmpty());
    }
}
