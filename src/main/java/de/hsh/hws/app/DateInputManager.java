package de.hsh.hws.app;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages reading and parsing date input from the user.
 * Each method has exactly one responsibility.
 * Throws exceptions directly for error handling.
 */
public class DateInputManager {

  private final Scanner scanner;
  private final PrintStream out;
  private static final Logger LOGGER = Logger.getLogger(DateInputManager.class.getName());

  public DateInputManager(Scanner scanner, PrintStream out) {
    this.scanner = scanner;
    this.out = out;
  }

  /**
   * Liest das Startdatum von der Konsole ein und gibt den eingegebenen String zurück.
   * Bei einem Fehler beim Lesen wird eine RuntimeException geworfen. "Fehler beim Lesen des Startdatums: "
   *
   * @return Benutzereingabe für das Startdatum
   */
  public String readStartDateInput() {
    try {
      out.print("Start date (yyyy-mm-dd): ");
      String line = scanner.nextLine();
      LOGGER.log(Level.FINE, "Read start date input: {0}", line);
      return line;
    } catch (RuntimeException e) {
      throw new RuntimeException("Fehler beim Lesen des Startdatums: " + e.getMessage());
    }
  }

  /**
   * Liest das Enddatum von der Konsole ein und gibt den eingegebenen String zurück.
   * Bei einem Fehler beim Lesen wird eine RuntimeException geworfen. "Fehler beim Lesen des Enddatums: "
   *
   * @return Benutzereingabe für das Enddatum
   */
  public String readEndDateInput() {
    try {
      out.print("End date (yyyy-mm-dd): ");
      String line = scanner.nextLine();
      LOGGER.log(Level.FINE, "Read end date input: {0}", line);
      return line;
    } catch (RuntimeException e) {
      throw new RuntimeException("Fehler beim Lesen des Enddatums: " + e.getMessage());
    }
  }


  
  /**
   * Parst die eingegebene Startdatum-Zeichenkette in ein LocalDate.
   * Bei ungültigem Format oder Datum wird eine RuntimeException geworfen. "Ungueltiges Startdatum. Bitte Format yyyy-mm-dd verwenden."
   *
   * @param startDateInput Eingabezeichenkette für das Startdatum
   * @return Geparstes LocalDate für das Startdatum
   */
  public LocalDate parseStartDate(String startDateInput) {
    try {
      LocalDate parsed = LocalDate.parse(startDateInput);
      LOGGER.log(Level.FINE, "Parsed start date: {0}", parsed);
      return parsed;
    } catch (DateTimeParseException e) {
      throw new RuntimeException("Ungueltiges Startdatum. Bitte Format yyyy-mm-dd verwenden.");
    }
  }

  /**
   * Parst die eingegebene Enddatum-Zeichenkette in ein LocalDate.
   * Bei ungültigem Format oder Datum wird eine RuntimeException geworfen. "Ungueltiges Enddatum. Bitte Format yyyy-mm-dd verwenden."
   *
   * @param endDateInput Eingabezeichenkette für das Enddatum
   * @return Geparstes LocalDate für das Enddatum
   */
  public LocalDate parseEndDate(String endDateInput) {
    try {
      LocalDate parsed = LocalDate.parse(endDateInput);
      LOGGER.log(Level.FINE, "Parsed end date: {0}", parsed);
      return parsed;
    } catch (DateTimeParseException e) {
      throw new RuntimeException("Ungueltiges Enddatum. Bitte Format yyyy-mm-dd verwenden.");
    }
  }
}
