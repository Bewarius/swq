package de.hsh.hws.app;

import de.hsh.hws.openmeteo.OpenMeteoService;
import de.hsh.hws.stats.WeatherStatisticsCalculator;
import de.hsh.hws.stats.WeatherStatisticsPrinter;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Main class for the Hannover Weather Statistics 2026 (hws) application.
 */
public class App {
  /**
   * Starting point of the application.
   * @param args console arguments.
   */
  public static void main(String[] args) {
    OpenMeteoService service = new OpenMeteoService();
    WeatherStatisticsCalculator calculator = new WeatherStatisticsCalculator();
    PrintStream out = System.out;
    WeatherStatisticsPrinter printer = new WeatherStatisticsPrinter(out);
    Scanner scanner = new Scanner(System.in);
    DateInputManager inputManager = new DateInputManager(scanner, out);

    WeatherCliController controller = new WeatherCliController(service, calculator, printer, inputManager, out);
    controller.run();

    // Legacy-Startercode absichtlich auskommentiert behalten.
    // OpenMeteoService service = new OpenMeteoService();
  }
}