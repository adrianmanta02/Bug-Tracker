package main.commands.advanced.search.dto;

import lombok.Getter;
import main.commands.advanced.search.SearchCommand;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.common.TicketType;

import java.time.LocalDate;
import java.util.List;

/**
 * main.commands.advanced.search.dto.TicketSearchResultDTO is a DTO, non-business logic
 * class used for serialization in case of a Ticket being searched by a Developer
 * after calling {@link SearchCommand} command. Provides also support for
 * case-insensitive searching keywords in order to enhance the results.
 */
@Getter
public final class TicketSearchResultDTO {
    private final int id;
    private final TicketType type;
    private final String title;
    private final BusinessPriority businessPriority;
    private final TicketStatus status;
    private final LocalDate createdAt;
    private final String solvedAt;
    private final String reportedBy;
    private final List<String> matchingWords;

    public TicketSearchResultDTO(final Ticket ticket,
                                 final List<String> matchingWords) {
        this.id = ticket.getId();
        this.type = ticket.getType();
        this.title = ticket.getTitle();
        this.businessPriority = ticket.getBusinessPriority();
        this.status = ticket.getStatus();
        this.createdAt = ticket.getCreatedAt();
        this.solvedAt = ticket.getSolvedAt();
        this.reportedBy = ticket.getReportedBy();
        this.matchingWords = matchingWords;
    }
}
