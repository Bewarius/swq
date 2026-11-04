package de.hsh.hws.stats;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculates statistics for hourly temperature data.
 */
public class WeatherStatisticsCalculator {

  private static final Logger LOGGER = Logger.getLogger(WeatherStatisticsCalculator.class.getName());

  /**
   * Calculates all required statistics from raw hourly values.
   *
   * @param values hourly temperature values, may contain NaN
   * @return calculated statistics
   */
  public WeatherStatistics calculate(double[] values) {
    if (values == null || values.length == 0) {
      return new WeatherStatistics(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0);
    }

    double[] validValues = Arrays.stream(values)
        .filter(v -> !Double.isNaN(v))
        .toArray();

    int totalCount = values.length;
    int validCount = validValues.length;
    int invalidCount = totalCount - validCount;
    LOGGER.log(Level.FINE, "Filtered values: total={0}, valid={1}, invalid={2}",
        new Object[] {totalCount, validCount, invalidCount});

    if (validValues.length == 0) {
      return new WeatherStatistics(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0);
    }


    // hier müssen wiwr nochmal ran damit das nicht so unübersichtlich wird
    double min = Arrays.stream(validValues).min().orElse(Double.NaN);
    double max = Arrays.stream(validValues).max().orElse(Double.NaN);
    double mean = Arrays.stream(validValues).average().orElse(Double.NaN);
    double median = calculateMedian(validValues);

    long coldCount = Arrays.stream(validValues).filter(v -> v < 0.0).count();
    long hotCount = Arrays.stream(validValues).filter(v -> v > 30.0).count();

    double coldPercentage = (coldCount * 100.0) / validValues.length;
    double hotPercentage = (hotCount * 100.0) / validValues.length;

    WeatherStatistics stats = new WeatherStatistics(min, max, mean, median, coldPercentage, hotPercentage);
    LOGGER.log(Level.FINE, "Computed statistics: min={0}, max={1}, mean={2}, median={3}, cold%={4}, hot%={5}",
      new Object[] {min, max, mean, median, coldPercentage, hotPercentage});
    return stats;
  }

  private double calculateMedian(double[] values) {
    double[] sorted = Arrays.copyOf(values, values.length);
    Arrays.sort(sorted);
    int middle = sorted.length / 2;

    if (sorted.length % 2 == 0) {
      return (sorted[middle - 1] + sorted[middle]) / 2.0;
    }
    return sorted[middle];
  }
}