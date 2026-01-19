package main.commands.advanced.search.strategies;

import main.commands.advanced.search.filters.utils.SearchContext;
import main.commands.advanced.search.filters.utils.SearchFilter;

/**
 * Represents a strategy for filtering items of type {@code T} based on a set of criteria.
 * Implementations define how to determine whether a given item matches the filtering rules
 * encapsulated in a {@link SearchFilter}. This allows flexible and reusable filtering
 * logic for different types of entities (e.g., tickets, developers).
 * @param <T> the type of items this filter strategy can evaluate
 */
public interface SearchFilterStrategy<T> {
    /**
     * Determines whether the given item satisfies the filter criteria.
     * @param itemToFilter the item to be evaluated against the filter
     * @param filter the {@link SearchFilter} containing the filtering criteria
     * @param context the {@link SearchContext} providing additional information
     *                required for the evaluation, such as user privileges or timestamp
     * @return {@code true} if the item matches the filter criteria, {@code false} otherwise
     */
    boolean matches(T itemToFilter, SearchFilter filter, SearchContext context);
}
