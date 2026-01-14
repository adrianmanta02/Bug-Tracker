package main.users.policies;

import main.ticket.Ticket;

public interface AccessPolicy {
    /**
     * @see main.users.User#canAccess(Ticket)
     */
    boolean canAccess(Ticket ticket);
}
