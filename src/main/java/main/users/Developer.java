package main.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.ticket.Ticket;
import main.users.enums.common.Role;
import main.users.enums.developer.Seniority;
import main.users.enums.developer.DeveloperSpecialization;
import main.users.policies.AccessPolicy;
import main.users.policies.SeniorityAccessFactory;

@Getter
@Setter
@NoArgsConstructor
public final class Developer extends User {
    private String hireDate;
    private DeveloperSpecialization expertiseArea;
    private Seniority seniority;
    @JsonIgnore
    private AccessPolicy ticketAccessStrategy;
    @JsonIgnore
    private double performanceScore = 0.0;

    public Developer(final String username, final String email,
                     final String hireDate, final DeveloperSpecialization expertiseArea,
                     final Seniority seniority) {
        super(username, email, Role.DEVELOPER);
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
        this.ticketAccessStrategy = SeniorityAccessFactory.createAccessPolicy(seniority);
    }

    /**
     * @see main.users.User#canAccess(Ticket)
     */
    public boolean canAccess(final Ticket ticket) {
        return ticketAccessStrategy.canAccess(ticket);
    }

    /**
     * @see main.users.User#checkAreas(Ticket)
     */
    public boolean checkAreas(final Ticket ticket) {
        return expertiseArea.canHandleArea(ticket.getExpertiseArea());
    }

    /**
     * @see main.users.User#canCommentOn(Ticket)
     */
    @Override
    public boolean canCommentOn(final Ticket ticket) {
        return ticket.getAssignedTo().equals(this.getUsername());
    }

    /**
     * Setter for Developer's Seniority.
     * @param seniority enum value for Seniority.
     */
    public void setSeniority(final Seniority seniority) {
        this.seniority = seniority;
        this.ticketAccessStrategy = SeniorityAccessFactory.createAccessPolicy(seniority);
    }
}
