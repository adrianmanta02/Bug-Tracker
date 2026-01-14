package main.users.enums.developer;

import main.ticket.enums.common.ExpertiseArea;

import java.util.List;
import lombok.Getter;

@Getter
public enum DeveloperSpecialization {
    FRONTEND(List.of(ExpertiseArea.FRONTEND, ExpertiseArea.DESIGN)),
    BACKEND(List.of(ExpertiseArea.BACKEND, ExpertiseArea.DB)),
    FULLSTACK(List.of(ExpertiseArea.FRONTEND, ExpertiseArea.BACKEND, ExpertiseArea.DEVOPS,
                    ExpertiseArea.DESIGN, ExpertiseArea.DB)),
    DEVOPS(List.of(ExpertiseArea.DEVOPS)),
    DESIGN(List.of(ExpertiseArea.DESIGN, ExpertiseArea.FRONTEND)),
    DB(List.of(ExpertiseArea.DB));

    private final List<ExpertiseArea> allowedAreas;
    DeveloperSpecialization(final List<ExpertiseArea> allowedAreas) {
        this.allowedAreas = allowedAreas;
    }

    /**
     * Checks if the allowed areas fields contain an expertise area given
     * @param areaToHandle enum {@link ExpertiseArea} value to be checked
     * @return a boolean verifying the matching.
     */
    public boolean canHandleArea(final ExpertiseArea areaToHandle) {
        return allowedAreas.contains(areaToHandle);
    }
}
