package de.hsh.hws.stats;

/**
 * Immutable container for computed weather statistics.
 */
public record WeatherStatistics(
    double min,
    double max,
    double mean,
    double median,
    double coldPercentage,
    double hotPercentage) {
}