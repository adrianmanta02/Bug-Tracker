package main.users;

import main.users.enums.common.Role;

public class Reporter extends User {
    public Reporter(final String username, final String email) {
        super(username, email, Role.DEVELOPER);
    }
}
