package main.commands.reports.metricbased.ticketrisk;

import static main.common.SystemConstants.MODERATE_RISK_THRESHOLD;
import static main.common.SystemConstants.SIGNIFICANT_RISK_THRESHOLD;
import static main.common.SystemConstants.MAJOR_RISK_THRESHOLD;

public final class RiskQualifier {
    private RiskQualifier() { }

    /**
     * Qualifies a numeric risk score into a qualitative risk level.
     * @param riskScore the numeric risk score to be qualified
     * @return a {@link RiskLevel} representing the qualitative risk level
     */
    public static RiskLevel qualify(final double riskScore) {
        if (riskScore >= MAJOR_RISK_THRESHOLD) {
            return RiskLevel.MAJOR;
        }

        if (riskScore >= SIGNIFICANT_RISK_THRESHOLD) {
            return RiskLevel.SIGNIFICANT;
        }

        if (riskScore >= MODERATE_RISK_THRESHOLD) {
            return RiskLevel.MODERATE;
        }

        return RiskLevel.NEGLIGIBLE;
    }
}
