package main.ticket.utils;

import java.util.List;

public abstract class ScoreCalculationStrategy {
    public double calculateImpactFinal(final double baseScore, final double maxValue) {
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }

    public double calculateAverageImpact(final List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
