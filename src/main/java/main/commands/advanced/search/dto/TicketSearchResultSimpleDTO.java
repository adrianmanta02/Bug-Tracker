package main.commands.advanced.search.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import main.commands.advanced.search.SearchCommand;
import main.ticket.Ticket;

/**
 * main.commands.advanced.search.dto.TicketSearchResultSimpleDTO is a DTO, non-business logic
 * class used for serialization in case of a Ticket being searched by a Developer
 * after calling {@link SearchCommand} command.
 */
@Getter
@JsonPropertyOrder({"id", "type", "title", "businessPriority", "status",
                "createdAt", "solvedAt", "reportedBy", "matchingWords"})
public final class TicketSearchResultSimpleDTO {
    private final int id;
    private final String type;
    private final String title;
    private final String businessPriority;
    private final String status;
    private final String createdAt;
    private final String solvedAt;
    private final String reportedBy;

    public TicketSearchResultSimpleDTO(final Ticket ticket) {
        this.id = ticket.getId();
        this.type = ticket.getType().toString();
        this.title = ticket.getTitle();
        this.businessPriority = ticket.getBusinessPriority().toString();
        this.status = ticket.getStatus().toString();
        this.createdAt = ticket.getCreatedAt().toString();
        this.solvedAt = ticket.getSolvedAt();
        this.reportedBy = ticket.getReportedBy();
    }
}
