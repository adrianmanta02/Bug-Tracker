package main.commands.advanced.search.filters.utils;

import main.commands.advanced.search.strategies.SearchFilterStrategy;
import main.commands.advanced.search.filters.ticketfilters.*;
import main.ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * main.commands.advanced.search.filters.utils.TicketFilterBuilder is a class responsible
 * for creating searching strategies given a set of criteria.
 */
public final class TicketFilterBuilder implements FilterBuilder<Ticket> {
    /**
     * @see FilterBuilder#build(SearchFilter)
     */
    @Override
    public List<SearchFilterStrategy<Ticket>> build(final SearchFilter filter) {
        List<SearchFilterStrategy<Ticket>> filters = new ArrayList<>();
        if (filter.getBusinessPriority() != null) {
            filters.add(new BusinessPrioritySearchFilter());
        }

        if (filter.getType() != null) {
            filters.add(new TicketTypeSearchFilter());
        }

        if (filter.getCreatedAt() != null) {
            filters.add(new CreatedAtSearchFilter());
        }

        if (filter.getCreatedAfter() != null) {
            filters.add(new CreatedAfterSearchFilter());
        }

        if (filter.getCreatedBefore() != null) {
            filters.add(new CreatedBeforeSearchFilter());
        }

        if (filter.getAvailableForAssignment() != null) {
            filters.add(new AvailableForAssignmentSearchFilter());
        }

        return filters;
    }
}
