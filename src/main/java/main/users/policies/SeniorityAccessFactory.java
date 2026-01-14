package main.users.policies;

import main.users.enums.developer.Seniority;

public final class SeniorityAccessFactory {
    private SeniorityAccessFactory() { }

    /**
     * Creates a ticket access strategy for a {@link main.users.Developer}, based on
     * their seniority. Each strategy comes with certain type of tickets to be solved,
     * having different difficulty levels.
     * @param developerSeniorityLevel enum showing developer's Seniority.
     * @return an instance of {@link AccessPolicy}.
     */
    public static AccessPolicy createAccessPolicy(final Seniority developerSeniorityLevel) {
        switch (developerSeniorityLevel) {
            case Seniority.JUNIOR -> {
                return new JuniorAccessPolicy();
            }
            case Seniority.MID -> {
                return new MidAccessPolicy();
            }
            case Seniority.SENIOR -> {
                return new SeniorAccessPolicy();
            }
            default -> {
                throw new IllegalArgumentException("Developer seniority provided, "
                        + developerSeniorityLevel
                        + ", does not exist!");
            }
        }
    }
}
