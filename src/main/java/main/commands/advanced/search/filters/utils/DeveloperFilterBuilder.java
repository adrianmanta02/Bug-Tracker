package main.commands.advanced.search.filters.utils;

import main.commands.advanced.search.strategies.SearchFilterStrategy;
import main.commands.advanced.search.filters.devfilters.ExpertiseSearchFilterStrategy;
import main.commands.advanced.search.filters.devfilters.PerformanceAboveSearchFilterStrategy;
import main.commands.advanced.search.filters.devfilters.PerformanceBelowSearchFilterStrategy;
import main.commands.advanced.search.filters.devfilters.SenioritySearchFilterStrategy;
import main.users.Developer;

import java.util.ArrayList;
import java.util.List;

public final class DeveloperFilterBuilder implements FilterBuilder<Developer> {
    /**
     * @see FilterBuilder#build(SearchFilter) 
     */
    @Override
    public List<SearchFilterStrategy<Developer>> build(final SearchFilter filter) {
        final List<SearchFilterStrategy<Developer>> filters = new ArrayList<>();

        if (filter.getPerformanceScoreBelow() != null) {
            filters.add(new PerformanceBelowSearchFilterStrategy());
        }

        if (filter.getPerformanceScoreAbove() != null) {
            filters.add(new PerformanceAboveSearchFilterStrategy());
        }

        if (filter.getExpertiseArea() != null) {
            filters.add(new ExpertiseSearchFilterStrategy());
        }

        if (filter.getSeniority() != null) {
            filters.add(new SenioritySearchFilterStrategy());
        }

        return filters;
    }
}
