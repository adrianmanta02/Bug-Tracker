package main.users;

import main.users.enums.common.Role;

public abstract class User {
    private String username;
    private String email;
    private Role role;

    public User(final String username, final String email, final Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
