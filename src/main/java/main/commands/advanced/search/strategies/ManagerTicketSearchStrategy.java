package main.commands.advanced.search.strategies;

import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.dto.TicketSearchResultDTO;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ManagerTicketSearchStrategy
        implements SearchStrategy<TicketSearchResultDTO> {
    /**
     * Searches for tickets in the {@link TicketContainer} according to a
     * set of filters applied by the {@link main.users.Manager} who is
     * calling the searching command.
     *
     * @param filter filters to be applied during the search
     * @param searchContext instance providing additional information for certain search
     *                      needs
     * @return a list of filtered Tickets.
     */
    @Override
    public List<TicketSearchResultDTO> search(final SearchFilter filter,
                                           final SearchContext searchContext) {

        List<Ticket> allTickets = TicketContainer.getInstance().getAllTickets();
        allTickets = applyFilters(filter, allTickets, searchContext);

        if (filter.getKeywords() != null) {
            return allTickets.stream()
                    .map(ticket -> createSearchResult(ticket, filter.getKeywords()))
                    .filter(result -> filter.getKeywords() == null
                            || !result.getMatchingWords().isEmpty())
                    .toList();
        }

        return allTickets.stream()
                .map(ticket -> new TicketSearchResultDTO(ticket, List.of()))
                .toList();
    }

    /**
     * Helper method that wraps the results of the searching process
     * in a DTO form to ensure further serialization of the items found
     * @param ticket ticket instance found amongst the filtered list
     * @param keywords additional words used as filter for finding a
     *                 Ticket by partial matches with its title/description
     * @return the filtered ticket as a DTO
     */
    private TicketSearchResultDTO createSearchResult(final Ticket ticket,
                                                  final List<String> keywords) {
        List<String> matchingWords = new ArrayList<>();

        if (keywords != null) {
            String title = ticket.getTitle();
            String description = ticket.getDescription();

            for (String keyword : keywords) {
                String match = keywordMatching(title, keyword);
                if (match != null && !matchingWords.contains(match)) {
                    matchingWords.add(match);
                }

                if (description != null) {
                    match = keywordMatching(description, keyword);
                    if (match != null && !matchingWords.contains(match)) {
                        matchingWords.add(match);
                    }
                }
            }

            matchingWords.sort(String::compareTo);
        }

        return new TicketSearchResultDTO(ticket, matchingWords);
    }

    /**
     * Finds the first word in the given text that contains the specified search keyword.
     * The text is split into words using whitespace as a delimiter. Each word is
     * compared to the search string using a case-insensitive substring match.
     * @param text the text to search in
     * @param search the keyword to search for
     * @return the first word containing the search keyword (case-insensitive),
     *         or {@code null} if no matching word is found
     */
    private String keywordMatching(final String text, final String search) {
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> word.toLowerCase().contains(search.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
