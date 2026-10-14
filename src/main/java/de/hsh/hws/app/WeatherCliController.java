package de.hsh.hws.app;

import de.hsh.hws.openmeteo.OpenMeteoService;
import de.hsh.hws.stats.WeatherStatistics;
import de.hsh.hws.stats.WeatherStatisticsCalculator;
import de.hsh.hws.stats.WeatherStatisticsPrinter;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


//import java.util.Optional benutzt wegen nullpointer exception vermeidung

/**
 * Orchestrates the CLI weather workflow.
 *
 * Verantwortlich für den Ablauf: Eingabe lesen, Daten validieren, Wetterdaten abrufen,
 * Statistiken berechnen und die Ausgabe anzeigen.
 */
public class WeatherCliController {

  private static final Logger LOGGER = Logger.getLogger(WeatherCliController.class.getName());

  private final OpenMeteoService service;
  private final WeatherStatisticsCalculator calculator;
  private final WeatherStatisticsPrinter printer;
  private final DateInputManager inputManager;
  private final PrintStream out;

  /**
   * Erstellt den Controller mit allen benötigten Komponenten für den Wetter-Workflow.
   *
   * @param service Service zum Abruf der Wetterdaten
   * @param calculator Berechner für Wetterstatistiken
   * @param printer Drucker für die berechneten Statistiken
   */
  public WeatherCliController(
      OpenMeteoService service,
      WeatherStatisticsCalculator calculator,
      WeatherStatisticsPrinter printer,
      DateInputManager inputManager,
      PrintStream out) {
    this.service = service;
    this.calculator = calculator;
    this.printer = printer;
    this.inputManager = inputManager;
    this.out = out;
  }

  /**
   * Führt den gesamten CLI-Workflow aus.
   *
   * <p>Der Ablauf umfasst: Scanner initialisieren, Datumseingaben lesen, das Datum validieren,
   * Wetterdaten abrufen, Statistiken berechnen und das Ergebnis ausgeben.
   */
  public void run() {
    printHeader();

    LOGGER.info("Starting Hannover Weather Statistics CLI");

    LocalDate startDate = readStartDate();
    LocalDate endDate = readEndDate();

    LOGGER.info("Requested period: " + startDate + " to " + endDate);

    //validiert das Datumsintervall.
    validateDateRange(startDate, endDate);

    WeatherStatistics statistics = calculateStatistics(startDate, endDate);
    displayStats(statistics);
  }

  void printHeader() {
    out.println("Hannover Weather Statistics: ");
  }

  LocalDate readStartDate() {
    String input = inputManager.readStartDateInput();
    return inputManager.parseStartDate(input);
  }

  LocalDate readEndDate() {
    String input = inputManager.readEndDateInput();
    return inputManager.parseEndDate(input);
  }

  void validateDateRange(LocalDate startDate, LocalDate endDate) {
    Optional<String> error = InputValidator.validateDateRange(startDate, endDate);
    if (error.isPresent()) {
      LOGGER.warning(error.get());
      throw new RuntimeException(error.get());
    }
  }

  WeatherStatistics calculateStatistics(LocalDate startDate, LocalDate endDate) {
    double[] temperatures = fetchWeatherData(startDate, endDate);
    return computeStatistics(temperatures);
  }

  public double[] fetchWeatherData(LocalDate startDate, LocalDate endDate) {
    try {
      double[] data = service.getTemperature(startDate, endDate);
      logFetchedWeatherData(data);
      return data;
    } catch (RuntimeException e) {
      throw createWeatherDataFetchException(e);
    }
  }

  private void logFetchedWeatherData(double[] data) {
    if (data == null) {
      return;
    }

    long nanCount = Arrays.stream(data).filter(d -> Double.isNaN(d)).count();
    LOGGER.log(Level.FINE, "Fetched {0} values ({1} NaN)", new Object[] {data.length, nanCount});
  }

  private RuntimeException createWeatherDataFetchException(RuntimeException cause) {
    String causeMessage = cause.getCause() == null ? cause.getMessage() : cause.getCause().getMessage();
    String message = causeMessage == null || causeMessage.isBlank()
        ? "Fehler beim Abruf der Wetterdaten"
        : "Fehler beim Abruf der Wetterdaten: " + causeMessage;
    LOGGER.log(Level.SEVERE, message, cause);
    return new RuntimeException(message, cause);
  }

  public WeatherStatistics computeStatistics(double[] temperatures) {
    return calculator.calculate(temperatures);
  }

  void displayStats(WeatherStatistics statistics) {
    printer.print(statistics);
  }
}


