package main.users;

import main.users.enums.common.Role;

import java.util.List;

public class Manager extends User {
    private String hireDate;
    private List<String> subordinates;

    public Manager(final String username, final String email,
                   final String hireDate, final List<String> subordinates) {
        super(username, email, Role.MANAGER);
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }
}
