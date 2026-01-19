package main.commands.advanced.search.strategies;

import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.dto.TicketSearchResultSimpleDTO;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.milestone.Milestone;
import main.milestone.MilestoneContainer;
import main.ticket.Ticket;

import java.util.List;

public final class DeveloperSearchStrategy implements SearchStrategy<TicketSearchResultSimpleDTO> {
    /**
     * @see SearchStrategy#search(SearchFilter, SearchContext)
     */
    public List<TicketSearchResultSimpleDTO> search(final SearchFilter filter,
                                                    final SearchContext searchContext) {
        final String devName = searchContext.getUser().getUsername();
        List<Milestone> milestonesAssigned = MilestoneContainer
                .getInstance()
                .getMilestonesAssociatedTo(devName);

        List<Ticket> openAssociatedTickets = milestonesAssigned
                .stream()
                .flatMap(milestone -> milestone.getOpenedTickets().stream())
                .toList();

        openAssociatedTickets = applyFilters(filter, openAssociatedTickets, searchContext);
        return openAssociatedTickets.stream()
                .map(TicketSearchResultSimpleDTO::new)
                .toList();
    }
}
