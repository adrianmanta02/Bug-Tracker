package main.ticket.actions;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ActionType {
    ASSIGNED("ASSIGNED"),
    DE_ASSIGNED("DE-ASSIGNED"),
    STATUS_CHANGED("STATUS_CHANGED"),
    ADDED_TO_MILESTONE("ADDED_TO_MILESTONE"),
    REMOVED_FROM_DEV("REMOVED_FROM_DEV");

    private final String displayName;
    ActionType(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter method for getting the associated String value
     * for each Enum value describing the type of action suffered
     * by the ticket; it is used while searching across
     * {@link main.ticket.Ticket}'s history, for serialization purposes.
     * @return the value assigned to the {@code ActionType}
     */
    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }
}
