package main.notification;

import main.commands.advanced.notifications.ViewNotificationsCommand;
import main.users.User;
import main.users.UserContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NotificationManager {
    private Map<String, List<Notification>> notificationMap = new HashMap<>();
    private static NotificationManager instance;

    private NotificationManager() {
        UserContainer
                .getInstance()
                .getUserList()
                .forEach(user -> notificationMap.put(user.getUsername(), new ArrayList<>()));
    }

    /**
     * Returns the Singleton instance of {@code NotificationManager}.
     * The instance is lazily initialized on the first invocation of this method.
     * @return the singleton {@code NotificationManager} instance
     */
    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Indicates whether the given user has any notifications.
     * This method implicitly initializes an empty notification list for the
     * specified username if none exists.
     * @param username the username to check for notifications
     * @return {@code true} if the user has at least one notification, {@code false} otherwise
     */
    private boolean hasNotifications(final String username) {
        return !getNotificationsByUsername(username).isEmpty();
    }

    /**
     * Returns the list of notifications associated with the given username.
     * If no notifications are currently mapped to the username, a new empty
     * list is created, stored in the internal notification map, and returned.
     * @param username the username whose notifications are to be retrieved
     * @return a non-null, mutable list of notifications for the given username
     */

    private List<Notification> getNotificationsByUsername(final String username) {
        return this.notificationMap.computeIfAbsent(username, u -> new ArrayList<>());
    }

    /**
     * Queries the notification messages by a given User instance.
     * @param user User object, the owner of the Notification messages.
     * @return list of String messages obtained after interrogating the NotificationManager
     */
    public List<String> getNotificationMessagesByUser(final User user) {
        return this.getNotificationsByUsername(user.getUsername())
                .stream()
                .map(Notification::getNotificationAlert)
                .toList();
    }

    /**
     * Queries the already existent notifications and adds a new instance to the list.
     * If already there, the notification won't be added due to spam avoiding purposes.
     * @param notification Notification object to add for User's notification List
     * @param username String identifier for User instance.
     */
    public void addNotificationToUser(final Notification notification, final String username) {
        List<Notification> existingNotifications = getNotificationsByUsername(username);

        boolean alreadyExists = existingNotifications
                .stream()
                .anyMatch(n -> n.getNotificationAlert()
                        .equals(notification.getNotificationAlert()));

        if (!alreadyExists) {
            existingNotifications.add(notification);
        }
    }

    /**
     * Deletes all notifications sent to a specific user, with the user being the key
     * inside the internal map. If existent, the list of notifications is erased.
     * This action is being called after the User seeing the notifications, done by
     * {@link ViewNotificationsCommand}
     * @param user User instance used as key to delete Comments
     */
    public void deleteUsersNotifications(final User user) {
        if (this.hasNotifications(user.getUsername())) {
            List<Notification> notifications = this.getNotificationsByUsername(user.getUsername());
            notifications.clear();
        }
    }

    /**
     * Resets the notification HashMap, useful between simulations to avoid
     * unwanted notifications caching from past sessions.
     */
    public void reset() {
        notificationMap.clear();
    }
}
