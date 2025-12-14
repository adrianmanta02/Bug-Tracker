package main.users.policies;

import main.ticket.Ticket;

public interface AccessPolicy {
    boolean canAccess(final Ticket ticket);
}
