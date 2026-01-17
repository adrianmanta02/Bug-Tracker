package main.commands.advanced.search.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import main.users.Developer;

/**
 * main.commands.advanced.search.DeveloperSearchResult is a DTO, non-business logic related
 * class which refers to the output obtained after a filtered search over the group
 * of Developers in a Manager's suborder.
 */
@Getter
@JsonPropertyOrder({"username", "expertiseArea", "seniority", "performanceScore", "hireDate"})
public class DeveloperSearchResultDTO {
    private final String username;
    private final String expertiseArea;
    private final String seniority;
    private final double performanceScore;
    private final String hireDate;

    public DeveloperSearchResultDTO(final Developer dev) {
        this.username = dev.getUsername();
        this.expertiseArea = dev.getExpertiseArea().toString();
        this.seniority = dev.getSeniority().toString();
        this.performanceScore = dev.getPerformanceScore();
        this.hireDate = dev.getHireDate();
    }
}
