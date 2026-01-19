package main.commands.advanced.search.filters.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;
import main.users.enums.developer.DeveloperSpecialization;
import main.users.enums.developer.Seniority;
import static main.common.SystemConstants.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * main.commands.advanced.search.SearchFilter is a wrapper class for
 * grouping all filters provided in
 * {@link main.commands.advanced.search.SearchCommand}.
 */
@Getter
public final class SearchFilter {
    private String searchType;
    private BusinessPriority businessPriority;
    private TicketType type;
    private LocalDate createdAt;
    private LocalDate createdBefore;
    private LocalDate createdAfter;
    private Boolean availableForAssignment;

    // manager-only fields
    private List<String> keywords;
    private DeveloperSpecialization expertiseArea;
    private Seniority seniority;
    private Double performanceScoreAbove;
    private Double performanceScoreBelow;

    /**
     * Method used for deserializing the filters from a JsonNode format,
     * building the final {@code SearchFilter} object to be used in the
     * searching command algorithm.
     * @param filtersNode JsonNode containing the filters provided
     * @return a search filter item used in the command.
     */
    public static SearchFilter fromJsonNode(final JsonNode filtersNode) {
        SearchFilter searchFilter = new SearchFilter();

        if (filtersNode.has(SEARCH_TYPE_FIELD)) {
            searchFilter.searchType = filtersNode.get(SEARCH_TYPE_FIELD).asText();
        }

        if (filtersNode.has(BUSINESS_PRIORITY_FIELD)) {
            searchFilter.businessPriority = BusinessPriority.valueOf(filtersNode
                    .get(BUSINESS_PRIORITY_FIELD)
                    .asText());
        }

        if (filtersNode.has(TYPE_FIELD)) {
            searchFilter.type = TicketType.valueOf(filtersNode.get(TYPE_FIELD).asText());
        }

        if (filtersNode.has(CREATED_AT_FIELD)) {
            searchFilter.createdAt = LocalDate.parse(filtersNode.get(CREATED_AT_FIELD).asText());
        }

        if (filtersNode.has(CREATED_BEFORE_FIELD)) {
            searchFilter.createdBefore = LocalDate.parse(filtersNode
                                                .get(CREATED_BEFORE_FIELD)
                                                .asText());
        }

        if (filtersNode.has(CREATED_AFTER_FIELD)) {
            searchFilter.createdAfter = LocalDate.parse(filtersNode
                                                .get(CREATED_AFTER_FIELD)
                                                .asText());
        }

        if (filtersNode.has(AVAILABLE_FIELD)) {
            searchFilter.availableForAssignment = filtersNode
                                                .get(AVAILABLE_FIELD)
                                                .asBoolean();
        }

        if (filtersNode.has(KEYWORDS_FIELD)) {
            searchFilter.keywords = new ArrayList<>();
            filtersNode.get(KEYWORDS_FIELD).forEach(keyword ->
                                        searchFilter.keywords.add(keyword.asText()));
        }

        if (filtersNode.has(EXPERTISE_AREA_FIELD)) {
            searchFilter.expertiseArea = DeveloperSpecialization.valueOf(filtersNode
                                                        .get(EXPERTISE_AREA_FIELD).asText());
        }

        if (filtersNode.has(SENIORITY_FIELD)) {
            searchFilter.seniority = Seniority.valueOf(filtersNode
                                                        .get(SENIORITY_FIELD)
                                                        .asText());
        }

        if (filtersNode.has(PERFORMANCE_ABOVE_FIELD)) {
            searchFilter.performanceScoreAbove = Double.valueOf(filtersNode
                                                    .get(PERFORMANCE_ABOVE_FIELD).asText());
        }

        if (filtersNode.has(PERFORMANCE_BELOW_FIELD)) {
            searchFilter.performanceScoreBelow = Double.valueOf(filtersNode
                                                    .get(PERFORMANCE_BELOW_FIELD).asText());
        }

        return searchFilter;
    }
}
