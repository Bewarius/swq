package de.hsh.hws.stats;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prints weather statistics to the console.
 */
public class WeatherStatisticsPrinter {

  private static final Logger LOGGER = Logger.getLogger(WeatherStatisticsPrinter.class.getName());
  private final PrintStream out;

  public WeatherStatisticsPrinter(PrintStream out) {
    this.out = out;
  }

  /**
   * Prints all fields with one decimal place.
   *
   * @param statistics calculated weather statistics
   */
  public void print(WeatherStatistics statistics) {
    String minLine = "Min. Temperature: " + formatOneDecimal(statistics.min()) + " °C";
    String maxLine = "Max. Temperature: " + formatOneDecimal(statistics.max()) + " °C";
    String meanLine = "Mean Temperature: " + formatOneDecimal(statistics.mean()) + " °C";
    String medianLine = "Median Temperature: " + formatOneDecimal(statistics.median()) + " °C";
    String coldLine = "Cold: " + formatOneDecimal(statistics.coldPercentage()) + " %";
    String hotLine = "Hot: " + formatOneDecimal(statistics.hotPercentage()) + " %";

    out.println(minLine);
    out.println(maxLine);
    out.println(meanLine);
    out.println(medianLine);
    out.println(coldLine);
    out.println(hotLine);

    LOGGER.log(Level.FINE, "Printed statistics:\n{0}\n{1}\n{2}\n{3}\n{4}\n{5}",
        new Object[] {minLine, maxLine, meanLine, medianLine, coldLine, hotLine});
  }

  private String formatOneDecimal(double value) {
    if (Double.isNaN(value)) {
      return "NaN";
    }
    return String.format("%.1f", value);
  }
}