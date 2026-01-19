package main.commands.reports.metricbased.stability;

public enum StabilityLevel {
    UNSTABLE("UNSTABLE"),
    PARTIALLY_STABLE("PARTIALLY STABLE"),
    STABLE("STABLE");

    private final String level;
    StabilityLevel(final String level) {
        this.level = level;
    }
}
