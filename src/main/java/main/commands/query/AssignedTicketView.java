package main.commands.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import main.comment.Comment;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.common.TicketType;
import java.time.LocalDate;
import java.util.List;

/*
    main.commands.query.AssignedTicketView is a DTO, a non business-logic class used for
    ViewAssignedTickets command's serialization, which does not use all of Ticket's fields
 */
@Getter
@JsonPropertyOrder({"id", "type", "title", "businessPriority", "status",
        "createdAt", "assignedAt", "reportedBy", "comments"})
public final class AssignedTicketView {
    private final int id;
    private final TicketType type;
    private final String title;
    private final BusinessPriority businessPriority;
    private final TicketStatus status;
    private final LocalDate createdAt;
    private final String assignedAt;
    private final String reportedBy;
    private final List<Comment> comments;

    public AssignedTicketView(final Ticket ticket) {
        this.id = ticket.getId();
        this.type = ticket.getType();
        this.title = ticket.getTitle();
        this.businessPriority = ticket.getBusinessPriority();
        this.status = ticket.getStatus();
        this.createdAt = ticket.getCreatedAt();
        this.assignedAt = ticket.getAssignedAt();
        this.reportedBy = ticket.getReportedBy();
        this.comments = ticket.getComments();
    }
}
