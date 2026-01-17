package main.milestone;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public final class MilestoneContainer {
    private static MilestoneContainer instance;
    private List<Milestone> milestoneList = new ArrayList<>();

    public MilestoneContainer() { }

    /**
     * Returns the Singleton instance of the container.
     * The instance is lazily initialized on the first invocation of this
     * method.
     * @return the singleton {@code MilestoneContainer} instance
     */
    public static MilestoneContainer getInstance() {
        if (instance == null) {
            instance = new MilestoneContainer();
        }
        return instance;
    }

    /**
     * Adds a new Milestone to the internal representation of the container
     * @param milestone Milestone instance to be added.
     */
    public void add(final Milestone milestone) {
        milestoneList.add(milestone);
    }

    /**
     * Checks across all Milestones to find if there is at least one
     * Milestone in which tickets are still not all solved by Developers.
     * @return {@code true} if there is at least one Milestone with the condition
     * given, {@code false} if Milestones have their Tickets solved.
     */
    public boolean hasActiveMilestones() {
        return this.milestoneList.stream().noneMatch(Milestone::areAllTicketsResolved);
    }

    /**
     * Gets the {@link Milestone} associated with a ticket unique identifier.
     * A ticket can be linked with only one Milestone at a time.
     * @param ticketId integer identifying the Ticket
     * @return Milestone instance containing the Ticket
     */
    public Milestone getMilestoneForTicket(final int ticketId) {
        return milestoneList.stream()
                .filter(milestone -> milestone.getTickets().contains(ticketId))
                .findFirst()
                .orElse(null);
    }

    /**
     * This method clears the contents of {@code MilestoneContainer} to avoid
     * caching the Milestones between the sessions.
     */
    public void reset() {
        this.milestoneList.clear();
    }

    /**
     * Retrieves all milestones associated with the specified user.
     * A milestone is considered associated with the user if the user's username
     * is included in the milestone's assigned developers.
     * @param username the username whose associated milestones are to be retrieved
     * @return a list of milestones associated with the given user;
     *         the list is empty if none are found
     */
    public List<Milestone> getMilestonesAssociatedTo(final String username) {
        return milestoneList
                .stream()
                .filter(milestone -> milestone.getAssignedDevs().contains(username))
                .toList();
    }

    /**
     * Returns the list of milestones.
     * The returned list is the internal representation and modifications to it
     * will directly affect this object
     * @return the list of milestones
     */
    public List<Milestone> getMilestones() {
        return milestoneList;
    }

    /**
     * Returns all milestones that are blocked by the given milestone.
     * A milestone is considered blocked if its name appears in the
     * blocking list of the provided milestone.
     * @param milestone the milestone that blocks others
     * @return a list of milestones blocked by the given milestone
     */
    public List<Milestone> getBlockedMilestonesBy(final Milestone milestone) {
        return this.milestoneList
                .stream()
                .filter(m -> milestone.getBlockingFor().contains(m.getName()))
                .toList();
    }

    /**
     * Searches across all milestones stored in order to find those that
     * are created by a certain {@link main.users.Manager}.
     * @param managerName the name of the manager owning the {@link Milestone}s
     * @return a list of milestones created by the manager
     */
    public List<Milestone> getMilestonesCreatedBy(final String managerName) {
        return this.milestoneList
                .stream()
                .filter(milestone -> milestone
                        .getCreatedBy()
                        .equals(managerName))
                .toList();
    }
}
