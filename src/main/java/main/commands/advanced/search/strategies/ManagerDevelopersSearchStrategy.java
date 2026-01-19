package main.commands.advanced.search.strategies;

import main.commands.advanced.search.SearchCommand;
import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.dto.DeveloperSearchResultDTO;
import main.commands.advanced.search.filters.utils.DeveloperFilterBuilder;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.users.Developer;
import main.users.Manager;
import main.users.UserContainer;

import java.util.Comparator;
import java.util.List;

/**
 * ManagerDevelopersSearchStrategy holds rules applied for
 * the searching process of a developer, made by the invoker
 * of the {@link SearchCommand}, the {@link Manager}.
 */
public final class ManagerDevelopersSearchStrategy
        implements SearchStrategy<DeveloperSearchResultDTO> {
    /**
     * @see SearchStrategy#search(SearchFilter, SearchContext)
     * */
    @Override
    public List<DeveloperSearchResultDTO> search(final SearchFilter filter,
                                                 final SearchContext searchContext) {
        Manager manager = (Manager) searchContext.getUser();
        List<Developer> subordinatedDevs = UserContainer
                .getInstance()
                .getManagerSubordinates(manager);

        DeveloperFilterBuilder builder = new DeveloperFilterBuilder();
        List<SearchFilterStrategy<Developer>> filters = builder.build(filter);

        return subordinatedDevs.stream()
                .filter(dev -> filters.stream()
                        .allMatch(filterToAdd ->
                                filterToAdd.matches(dev, filter, searchContext)))
                .sorted(Comparator.comparing(Developer::getUsername))
                .map(DeveloperSearchResultDTO::new)
                .toList();
    }
}
