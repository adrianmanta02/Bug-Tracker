package main.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.ticket.Ticket;
import main.users.enums.common.Role;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class Manager extends User {
    private String hireDate;
    private List<String> subordinates;

    public Manager(final String username, final String email,
                   final String hireDate, final List<String> subordinates) {
        super(username, email, Role.MANAGER);
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }

    /**
     * @see main.users.User#canCommentOn(Ticket)
     */
    @Override
    public boolean canCommentOn(final Ticket ticket) {
        return false;
    }

    /**
     * @see main.users.User#canAccess(Ticket)
     */
    @Override
    public boolean canAccess(final Ticket ticket) {
        return false;
    }

    /**
     * @see main.users.User#checkAreas(Ticket)
     */
    @Override
    public boolean checkAreas(final Ticket ticket) {
        return false;
    }
}
