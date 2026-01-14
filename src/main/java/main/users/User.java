package main.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.notification.Notification;
import main.notification.NotificationManager;
import main.ticket.Ticket;
import main.users.enums.common.Role;

@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    private String username;
    private String email;
    private Role role;

    public User(final String username, final String email, final Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    /**
     * Method being part of Observer Pattern. It automatically updates users in case of a
     * {@link Notification} alert cause by Milestones.
     * @param notification Notification instance sent to User.
     */
    public void update(final Notification notification) {
        NotificationManager.getInstance().addNotificationToUser(notification, this.username);
    }

    /**
     * Checks if current user can leave comments on a Ticket. It is an
     * action dependent on their role.
     * @param ticket instance of Ticket to make the check against.
     * @return a boolean showing User's capability to leave comments.
     */
    public abstract boolean canCommentOn(Ticket ticket);

    /**
     * Checks if current User can take on a ticket.
     * @param ticket instance of Ticket to make the check against.
     * @return a boolean showing User's capability on handling that Ticket.
     */
    public abstract boolean canAccess(Ticket ticket);

    /**
     * Checks if current User can access tickets based on
     * {@link main.ticket.enums.common.ExpertiseArea}.
     * @param ticket instance of Ticket to make the check against.
     * @return a boolean showing User's capability on handling that Ticket's expertise area.
     */
    public abstract boolean checkAreas(Ticket ticket);
}
