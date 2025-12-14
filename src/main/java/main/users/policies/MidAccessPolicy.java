package main.users.policies;

import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

public class MidAccessPolicy implements AccessPolicy {
    @Override
    public boolean canAccess(final Ticket ticket) {
        return (ticket.getType() == TicketType.BUG
                    || ticket.getType() == TicketType.UI_FEEDBACK
                    || ticket.getType() == TicketType.FEATURE_REQUEST)
                && (ticket.getBusinessPriority() == BusinessPriority.LOW
                    || ticket.getBusinessPriority() == BusinessPriority.MEDIUM
                    || ticket.getBusinessPriority() == BusinessPriority.HIGH);
    }
}
