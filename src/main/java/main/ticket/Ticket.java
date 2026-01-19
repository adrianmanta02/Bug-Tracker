package main.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.parsing.ParserUtils;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.ExpertiseArea;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.common.TicketType;

import lombok.Getter;
import lombok.Setter;
import main.comment.Comment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Ticket {
    private int id;
    private TicketType type;
    private String title;
    private BusinessPriority businessPriority;
    private TicketStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    private LocalDate assignedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate solvedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assignedTo;

    private String reportedBy;

    @JsonIgnore
    private ExpertiseArea expertiseArea;

    @JsonIgnore
    private String description;

    @JsonIgnore
    private double score;
    private List<Comment> comments = new ArrayList<>();

    public static abstract class TicketBuilder<T extends Ticket, B extends TicketBuilder<T, B>> {
        private final int id;
        private final TicketType type;
        private final String title;
        private final BusinessPriority businessPriority;
        private final TicketStatus status;
        private final ExpertiseArea expertiseArea;

        private String description;
        private String reportedBy;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate createdAt;

        public TicketBuilder(final int id, final TicketType type, final String title,
                             final BusinessPriority businessPriority,
                             final ExpertiseArea expertiseArea,
                             final LocalDate createdAt) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.businessPriority = businessPriority;
            this.status = TicketStatus.OPEN;
            this.expertiseArea = expertiseArea;
            this.createdAt = createdAt;
        }

        /**
         * Adds optional field description
         * @param desc String field
         * @return enhanced builder instance with field added.
         */
        public B description(final String desc) {
            this.description = desc;
            return self();
        }

        /**
         * Adds optional field reportedBy
         * @param reported String field
         * @return enhanced builder instance with field added.
         */
        public B reportedBy(final String reported) {
            this.reportedBy = reported;
            return self();
        }

        /**
         * Method used to get hold of the builder. The Ticket object's creation
         * relies on having an inherited builder, since a lot of its mandatory fields
         * are common across other Ticket types.
         * @return instance of the builder used.
         */
        protected abstract B self();

        /**
         * Generic method for Builder Pattern. Used after setting all optional parameters
         * and wanting to create the final object.
         * @return instance of object built
         */
        protected abstract T build();
    }

    public Ticket(final TicketBuilder<?, ?> ticketBuilder) {
        this.id = ticketBuilder.id;
        this.type = ticketBuilder.type;
        this.title = ticketBuilder.title;
        this.businessPriority = ticketBuilder.businessPriority;
        this.status = ticketBuilder.status;
        this.expertiseArea = ticketBuilder.expertiseArea;
        this.description = ticketBuilder.description;
        this.reportedBy = ticketBuilder.reportedBy;
        this.createdAt = ticketBuilder.createdAt;
    }

    /**
     * Checks if a ticket was reported by a User.
     * @return a boolean value showing the value of truth for being reported by someone.
     */
    @JsonIgnore
    public boolean isAnonymous() {
        return this.reportedBy == null || this.reportedBy.isEmpty();
    }

    /**
     * Adds a Comment object to Ticket's 'comment section'
     * @param comment Comment instance to be added.
     */
    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    /**
     * Removes a Comment instance from Ticket. Users can provide comment on
     * Tickets that might help the Developer track their work better.
     * Each ticket have a 'comment section' associated.
     * @param comment Comment instance being deleted
     */
    public void removeComment(final Comment comment) {
        this.comments.remove(comment);
    }

    /**
     * Checks if Ticket's status is set on OPEN.
     * @return boolean value
     */
    @JsonIgnore
    public boolean isOpen() {
        return this.getStatus() == TicketStatus.OPEN;
    }

    /**
     * Checks if a Ticket is assigned to a Developer
     * @param developerUsername the name of the Developer.
     * @return a boolean value indicating the value of truth for the affirmation.
     */
    @JsonIgnore
    public boolean isAssignedTo(final String developerUsername) {
        return developerUsername.equals(this.assignedTo);
    }

    /**
     * Sets Ticket's field according to the assignation of it via AssignTicketCommand.
     * @param developerUsername the name of the developer responsible to solve the Ticket
     * @param timestamp the exact moment in time the assignation took place.
     */
    public void assign(final String developerUsername, final LocalDate timestamp) {
        this.setAssignedTo(developerUsername);
        this.setStatus(TicketStatus.IN_PROGRESS);
        this.setAssignedAt(timestamp);
    }

    /**
     * This method is responsible for setting Ticket's internals after it being unassigned
     * by UndoAssignTicketCommand. It deletes any developer name details as well as it sets
     * the status to open, meaning no developer is currently working on it.
     */
    public void unassign() {
        this.setAssignedTo("");
        this.setStatus(TicketStatus.OPEN);
        this.setAssignedAt(null);
    }

    /**
     * Generic method for applying the Visitor Pattern in order to get hold
     * of some of Ticket's fields needed for multiple report calculations.
     * @param visitor the visitor instance responsible for getting the fields needed.
     * @return the calculated result needed for further report-related computations.
     */
    public abstract double accept(TicketMetricVisitor visitor);

    /**
     * Calculates the days necessary to solve the Ticket by the developer that was in
     * charge of the ticket.
     * @return the day difference between the date in which the ticket was assigned to a Developer
     * and the date in which the Ticket was successfully solved, or -1 if one of the dates is
     * unavailable, meaning the ticket was not assigned or was not solved (or both).
     */
    @JsonIgnore
    public int getDaysToResolve() {
        if (assignedAt == null || solvedAt == null) {
            return -1;
        }

        return ParserUtils.dayDifference(assignedAt, solvedAt);
    }


    /**
     * Checks if the current status of the ticket is on
     * {@link TicketStatus#RESOLVED}, in order to set its {@code solvedAt}
     * field with the given timestamp. This field only updates on the first
     * status changing on RESOLVED that is detected.
     * @param timestamp moment in time in which the status changes
     */
    public void checkForSolved(final LocalDate timestamp) {
        if (this.getStatus() == TicketStatus.RESOLVED) {
            this.setSolvedAt(timestamp);
        }
    }

    /**
     * Checks if a Ticket's status is set on IN_PROGRESS.
     * @return boolean value showing the value of truth for the affirmation.
     */
    @JsonIgnore
    public boolean isInProgress() {
        return this.status == TicketStatus.IN_PROGRESS;
    }

    /**
     * Checks if a Ticket's status is set on CLOSED.
     * @return boolean value showing the value of truth for the affirmation.
     */
    @JsonIgnore
    public boolean isClosed() {
        return this.status == TicketStatus.CLOSED;
    }

    /**
     * This method changes the status for a ticket with the next available status.
     * Once reaching CLOSED state, the status will not be updated anymore by the method,
     * meaning it will stay the same (unless the UndoChangeCommand is called).
     */
    public void changeStatus() {
        this.setStatus(status.next());
    }

    /**
     * Custom getter for 'assignedTo' field, showing the username for the developer
     * that should be in charge of a Ticket.
     * @return a string representing the name of the developer if the assignation took place,
     * or an empty string if there is not yet a developer assigned to the current ticket.
     */
    public String getAssignedTo() {
        return this.assignedTo != null ? this.assignedTo : "";
    }

    /**
     * Custom getter for 'solvedAt' field, showing the date at which a developer
     * has finished working on a Ticket.
     * @return a string representing the conversion from LocalDate if the ticket
     * was successfully solved, or an empty string if the ticket hasn't been solved yet.
     */
    public String getSolvedAt() {
        return this.solvedAt != null ? this.solvedAt.toString() : "";
    }

    /**
     * Getter for field 'assignedAt', showing the date at which a developer started working
     * on a Ticket.
     * @return a string representing the conversion from LocalDate if ticket was assigned, or
     * an empty string if the ticket was not taken on by any Developer.
     */
    public String getAssignedAt() {
        return this.assignedAt != null ? this.assignedAt.toString() : "";
    }
}
