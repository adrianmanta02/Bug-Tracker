package main.users.policies;

import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

public class JuniorAccessPolicy implements AccessPolicy {
    /**
     * @see main.users.User#canAccess(Ticket)
     */
    @Override
    public boolean canAccess(final Ticket ticket) {
        return (ticket.getType() == TicketType.BUG
                    || ticket.getType() == TicketType.UI_FEEDBACK)
                && (ticket.getBusinessPriority() == BusinessPriority.LOW
                    || ticket.getBusinessPriority() == BusinessPriority.MEDIUM);
    }
}
