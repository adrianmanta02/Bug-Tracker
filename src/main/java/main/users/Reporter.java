package main.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.ticket.Ticket;
import main.users.enums.common.Role;

@Getter
@Setter
@NoArgsConstructor
public final class Reporter extends User {
    public Reporter(final String username, final String email) {
        super(username, email, Role.REPORTER);
    }

    /**
     * @see User#canCommentOn(Ticket)
     */
    @Override
    public boolean canCommentOn(final Ticket ticket) {
        return ticket.getReportedBy().equals(this.getUsername());
    }

    /**
     * @see User#canAccess(Ticket)
     */
    @Override
    public boolean canAccess(final Ticket ticket) {
        return false;
    }

    /**
     * @see User#checkAreas(Ticket)
     */
    @Override
    public boolean checkAreas(final Ticket ticket) {
        return false;
    }
}
