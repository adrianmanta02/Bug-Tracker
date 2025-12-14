package main.users.policies;

import main.users.enums.developer.Seniority;

public class SeniorityAccessFactory {
    public static AccessPolicy createAccessPolicy(final Seniority developerSeniorityLevel) {
        switch(developerSeniorityLevel) {
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
