package main.commands.reports.metricbased.stability;

import main.commands.reports.metricbased.ticketrisk.RiskLevel;
import main.ticket.enums.common.TicketType;

import java.util.Map;

import static main.common.SystemConstants.IMPACT_THRESHOLD;

public final class StabilityCalculator {
    private StabilityCalculator() { }

    /**
     * Fetches data related to tickets and gives a visual
     * feedback related to the overall stability of the application.
     * There are three levels of measuring the stability, each being
     * determined either by the pre-defined metrics or by other
     * internal computations.
     * @param totalOpenTickets the number of open tickets reported
     * @param riskByType a labeled map containing the repartition of tickets
     *                   on risk levels
     * @param impactByType a labeled map containing the repartition of tickets
     *                     on impact coefficients
     * @return a String showing the stability level which the app has gained
     * after the analysis.
     */
    public static String determineStability(
            final int totalOpenTickets,
            final Map<TicketType, RiskLevel> riskByType,
            final Map<TicketType, Double> impactByType) {

        if (totalOpenTickets == 0) {
            return StabilityLevel.STABLE.toString();
        }

        if (hasSignificantRisk(riskByType)) {
            return StabilityLevel.UNSTABLE.toString();
        }

        if (allNegligibleRisk(riskByType) && allImpactBelow50(impactByType)) {
            return StabilityLevel.STABLE.toString();
        }

        return StabilityLevel.PARTIALLY_STABLE.toString();
    }

    /**
     * Checks whether any ticket type has a significant risk level.
     * A risk is considered significant if its value is either {@code "SIGNIFICANT"}
     * or {@code "MAJOR"}.
     * @param riskByType a map from ticket type names to their corresponding risk levels
     * @return {@code true} if at least one ticket type has a significant risk,
     * {@code false} otherwise
     */
    private static boolean hasSignificantRisk(final Map<TicketType, RiskLevel> riskByType) {
        return riskByType.values()
                .stream()
                .anyMatch(risk -> RiskLevel.SIGNIFICANT == risk || RiskLevel.MAJOR == risk);
    }

    /**
     * Checks whether all ticket types have negligible risk.
     * A risk is considered negligible if its value is {@code "NEGLIGIBLE"}.
     * @param riskByType a map from ticket type names to their corresponding risk levels
     * @return {@code true} if all ticket types are negligible, {@code false} otherwise
     */
    private static boolean allNegligibleRisk(final Map<TicketType, RiskLevel> riskByType) {
        return riskByType.values()
                .stream()
                .allMatch(riskLevel -> riskLevel == RiskLevel.NEGLIGIBLE);
    }

    /**
     * Checks whether all ticket types have a customer impact below 50%.
     * @param impactByType a map from ticket type names to their corresponding impact scores
     * @return {@code true} if all impact scores are below 50.0, {@code false} otherwise
     */
    private static boolean allImpactBelow50(final Map<TicketType, Double> impactByType) {
        return impactByType.values().stream()
                .allMatch(impact -> impact < IMPACT_THRESHOLD);
    }
}
