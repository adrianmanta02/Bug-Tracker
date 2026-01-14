package main.users;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import main.users.enums.common.Role;

@Getter
public final class UserContainer {
    private static UserContainer instance;
    private List<User> userList = new ArrayList<>();
    private UserContainer() { }

    /**
     * Singleton static method to get the unique instance for the container.
     * @return Singleton instance
     */
    public static UserContainer getInstance() {
        if (instance == null) {
            instance = new UserContainer();
        }
        return instance;
    }

    /**
     * Adds a {@link User} to the Container.
     * @param user User instance to be added.
     */
    public void add(final User user) {
        this.userList.add(user);
    }

    /**
     * Checks if the container has a User with a given username.
     * @param username string representing the username of the User searched
     * @return a boolean showing the existence of the User.
     */
    public boolean exists(final String username) {
        return this.userList.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Does a query to get the User instance from a given username. This query is general.
     * @param username string representing the username of the User searched
     * @return {@link User} instance
     */
    public User getUserByUsername(final String username) {
        return this.userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    /**
     * Query made in order to get hold of {@link User} instances by a list of usernames
     * @param usernameList list of strings representing the usernames for User instances
     * @return List of {@link User}.
     */
    public List<User> getUsersByUsernames(final List<String> usernameList) {
        return this.userList
                .stream()
                .filter(user -> usernameList.contains(user.getUsername()))
                .toList();
    }

    /**
     * Clears up the container usable List.
     */
    public void reset() {
        this.userList.clear();
    }

    public List<Developer> getDevs() {
        return this
                .userList
                .stream()
                .filter(user -> user.getRole() == Role.DEVELOPER)
                .map(user -> (Developer) user)
                .toList();
    }

    public List<Manager> getManagers() {
        return this
                .userList
                .stream()
                .filter(user -> user.getRole() == Role.MANAGER)
                .map(user -> (Manager) user)
                .toList();
    }

    /**
     * Does a query to get the User role-based instance, Developer, from a given username.
     * @param username string representing the username of the Manager searched
     * @return {@link Developer} instance
     */
    public Developer getDeveloperByUsername(final String username) {
        return getDevs()
                .stream()
                .filter(dev -> dev.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Does a query to get the User role-based instance, Manager, from a given username.
     * @param username string representing the username of the Manager searched
     * @return {@link Manager} instance
     */
    public Manager getManagerByUsername(final String username) {
        return getManagers()
                .stream()
                .filter(manager -> manager.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Queries the manager's subordinates. Each manager has a list of
     * usernames for the {@link Developer}s that are assigned to the
     * milestones created by the manager.
     * @param manager the {@link Manager} instance
     * @return a list of Developer instances with usernames matching the
     * ones in the list 'assignedDevs'
     */
    public List<Developer> getManagerSubordinates(final Manager manager) {
        return getDevs()
                .stream()
                .filter(dev -> manager.getSubordinates().contains(dev.getUsername()))
                .toList();
    }
}
