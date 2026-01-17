package main.commands.advanced.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.notification.NotificationManager;
import main.users.enums.common.Role;

import java.util.List;

public final class ViewNotificationsCommand extends Command {
    public ViewNotificationsCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    /**
     * Finds and displays all notifications for a certain user.
     * All its notifications are searched in the notification systems;
     * once seen, these are deleted from the internal storing form of
     * the {@link NotificationManager}.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return {@link CommandResult} which emphasizes the notifications
     * received up to the point of requesting the command for
     * a {@link main.users.User}.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        NotificationManager notificationManager = NotificationManager.getInstance();
        List<String> notificationAlerts = notificationManager
                .getNotificationMessagesByUser(this.getInvoker());

        NotificationManager.getInstance().deleteUsersNotifications(this.getInvoker());

        return new CommandResult.CommandResultBuilder(
            this.command,
            this.username,
            this.timestamp
        ).data("notifications", notificationAlerts)
        .build();
    }
}
