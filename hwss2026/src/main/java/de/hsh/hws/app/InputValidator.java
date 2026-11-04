package de.hsh.hws.app;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Validator utility for user input.
 */
public final class InputValidator {

  /**
   * Private Konstruktor, um die Klasse als Utility-Klasse zu kennzeichnen.
   * Die Klasse darf nicht instanziiert werden.
   */
  private InputValidator() {
    // Utility class.
  }

  /**
   * validiert Gültigkeit eines Datumsbereichs.
   *
   * Prüft, ob das Startdatum vor oder am Enddatum liegt und dass das Enddatum nicht in der
   * Zukunft liegt.
   *
   * @param startDate start date
   * @param endDate end date
   * @return Optional.empty() if valid, otherwise an Optional containing an error message
   */
  public static Optional<String> validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      return Optional.of("Ungueltiger Zeitraum: Startdatum liegt nach dem Enddatum.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      return Optional.of("Ungueltiger Zeitraum: Enddatum darf nicht in der Zukunft liegen.");
    }

    // Separate check for incomplete data on today's date
    Optional<String> incompleteDataError = validateIncompleteDate(endDate);
    if (incompleteDataError.isPresent()) {
      return incompleteDataError;
    }

    return Optional.empty();
  }

  /**
   * Checks if the end date is today, which would result in incomplete weather data.
   * The Open-Meteo API returns NaN values for future hours, which could skew statistics.
   *
   * @param endDate the end date to check
   * @return Optional.empty() if date is not today, otherwise an Optional containing an error message
   */
  private static Optional<String> validateIncompleteDate(LocalDate endDate) {
    if (endDate.isEqual(LocalDate.now())) {
      return Optional.of(
          "Ungueltiger Zeitraum: Enddatum darf nicht heute sein. "
              + "Heutige Daten sind unvollstaendig und verfaelschen die Statistiken. "
              + "Bitte Vortag als Enddatum waehlen.");
    }
    return Optional.empty();
  }
}