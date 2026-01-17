package main.notification;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Notification {
    protected String notificationAlert;
    protected List<String> receivers = new ArrayList<>();

    public Notification(final String notificationAlert) {
        this.notificationAlert = notificationAlert;
    }
}
