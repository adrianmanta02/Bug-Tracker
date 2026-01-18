package main.commands.advanced.search.filters.utils;

import main.commands.advanced.search.strategies.SearchFilterStrategy;

import java.util.List;

/**
 * main.commands.advanced.search.FilterBuilder is an interface
 * which offers a common contract for building a filter, for both
 * {@link main.ticket.Ticket} and {@link main.users.Developer},
 * based on given criteria.
 * @param <T> the object being filtered.
 */
public interface FilterBuilder<T> {
    /**
     * Creates and return a list of filters after checking
     * against the criteria given. Once built, the set of rules
     * are being applied on searching algorithms regarding
     * the objects being filtered.
     * @param filter instance containing all collected criteria
     * @return a {@link java.util.List} of {@link SearchFilterStrategy}
     * providing all rules selected based on the input.
     */
    List<SearchFilterStrategy<T>> build(SearchFilter filter);
}
