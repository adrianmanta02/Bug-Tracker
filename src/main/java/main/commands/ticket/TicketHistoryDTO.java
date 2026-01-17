package main.commands.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import main.comment.Comment;
import main.ticket.Ticket;
import main.ticket.actions.TicketAction;
import main.ticket.enums.common.TicketStatus;

import java.time.LocalDate;
import java.util.List;


/*
    main.commands.ticket.TicketHistoryDTO is a data transfer object, a non business-logic class
    used forViewTicketHistory command's serialization, which does not use all of Ticket's
    fields
 */
@Getter
@JsonPropertyOrder({"id", "title", "status", "actions", "comments"})
public final class TicketHistoryDTO {
    private final int id;
    private final String title;
    private final TicketStatus status;
    private final List<TicketAction> actions;
    private final List<Comment> comments;

    @JsonIgnore
    private final LocalDate createdAt;

    public TicketHistoryDTO(final Ticket ticket, final List<TicketAction> actions) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.status = ticket.getStatus();
        this.actions = actions;
        this.comments = ticket.getComments();
        this.createdAt = ticket.getCreatedAt();
    }
}
