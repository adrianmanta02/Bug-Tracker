package main.users;

import main.ticket.Ticket;
import main.users.enums.common.Role;
import main.users.enums.developer.Seniority;
import main.users.enums.developer.DeveloperSpecialization;
import main.users.policies.AccessPolicy;
import main.users.policies.SeniorityAccessFactory;

public class Developer extends User {
    private String hireDate;
    private DeveloperSpecialization expertiseArea;
    private Seniority seniority;
    private AccessPolicy ticketAccessStrategy;

    public Developer(final String username, final String email,
                     final String hireDate, final DeveloperSpecialization expertiseArea,
                     final Seniority seniority) {
        super(username, email, Role.DEVELOPER);
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
        this.ticketAccessStrategy = SeniorityAccessFactory.createAccessPolicy(seniority);
    }

    public boolean canAccess(final Ticket ticket) {
        return ticketAccessStrategy.canAccess(ticket);
    }

    public boolean checkAreas(final Ticket ticket) {
        return expertiseArea.canHandleArea(ticket.getExpertiseArea());
    }
}
