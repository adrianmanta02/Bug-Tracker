package main.commands.advanced.search.filters.ticketfilters;

import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.strategies.SearchFilterStrategy;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.ticket.Ticket;

public final class CreatedAfterSearchFilter implements SearchFilterStrategy<Ticket> {
    /**
     * @see SearchFilterStrategy#matches(Object, SearchFilter, SearchContext)
     */
    @Override
    public boolean matches(final Ticket ticket, final SearchFilter filter, SearchContext context) {
        return filter.getCreatedAfter() == null
                || ticket.getCreatedAt().isAfter(filter.getCreatedAfter());
    }
}
