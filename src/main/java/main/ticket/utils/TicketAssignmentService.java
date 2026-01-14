package main.ticket.utils;

import main.ticket.Ticket;
import main.ticket.enums.common.ExpertiseArea;
import main.users.enums.developer.DeveloperSpecialization;
import main.users.enums.developer.Seniority;
import main.users.policies.AccessPolicy;
import main.users.policies.SeniorityAccessFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class TicketAssignmentService {
    private TicketAssignmentService() {
    }

    /**
     * Determines which {@link DeveloperSpecialization} values are compatible
     * with the given ticket.
     * A specialization is considered compatible if it can handle the
     * {@link ExpertiseArea} required by the ticket.
     * The resulting list is sorted alphabetically by specialization name.
     * @param ticket the ticket whose required expertise is evaluated
     * @return a list of compatible developer specializations
     */
    private static List<DeveloperSpecialization>
                getCompatibleDevSpecializations(final Ticket ticket) {
        final ExpertiseArea expertiseArea = ticket.getExpertiseArea();

        return Arrays.stream(DeveloperSpecialization.values())
                .filter(specialization -> specialization.canHandleArea(expertiseArea))
                .sorted(Comparator.comparing(Enum::name))
                .toList();
    }

    /**
     * Retrieves the names of all developer specializations compatible with the given ticket.
     * This method filters all available developer specializations to find those that can
     * handle the expertise area required by the ticket, and returns their names as strings
     * in alphabetical order.
     *
     * @param ticket the ticket for which compatible developer specializations are needed
     * @return a list of specialization names that are compatible with the ticket's expertise area
     */
    public static List<String> getCompatibleDevSpecializationNames(final Ticket ticket) {
        return getCompatibleDevSpecializations(ticket)
                .stream()
                .map(DeveloperSpecialization::name)
                .toList();
    }

    /**
     * Extracts the required seniority levels in order to be able to solve a Ticket.
     * @param ticket Ticket instance
     * @return list of {@link Seniority} levels.
     */
    private static List<Seniority> getRequiredSeniorityLevels(final Ticket ticket) {
        return Arrays.stream(Seniority.values())
                .filter(seniority -> {
                    final AccessPolicy policy = SeniorityAccessFactory
                            .createAccessPolicy(seniority);
                    return policy.canAccess(ticket);
                })
                .toList();
    }

    /**
     * Extracts the {@link Seniority} levels associated with a Ticket
     * These levels are required for a {@link main.users.Developer}
     * in order to be able to have a {@link Ticket} assigned.
     * @param ticket the ticket for which the requirements for a developer
     *               are being retrieved
     * @return list of String names for all {@link Seniority} levels.
     */
    public static List<String> getRequiredSeniorityLevelNames(final Ticket ticket) {
        return getRequiredSeniorityLevels(ticket)
                .stream()
                .map(Seniority::name)
                .toList();
    }
}
