package main.commands.advanced.search.filters.devfilters;

import main.commands.advanced.search.filters.utils.SearchFilter;
import main.commands.advanced.search.strategies.SearchFilterStrategy;
import main.commands.advanced.search.filters.utils.SearchContext;
import main.users.Developer;

public final class PerformanceAboveSearchFilterStrategy implements SearchFilterStrategy<Developer> {
    /**
     * @see SearchFilterStrategy#matches(Object, SearchFilter, SearchContext)
     */
    @Override
    public boolean matches(final Developer itemToFilter, final SearchFilter filter, SearchContext context) {
        return itemToFilter.getPerformanceScore() == 0.0
                || itemToFilter.getPerformanceScore() >= filter.getPerformanceScoreAbove();
    }
}
