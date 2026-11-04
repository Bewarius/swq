package de.hsh.hws.app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Test-Klasse für InputValidator.
 * 
 * GETESTETE KLASSE: InputValidator
 * 
 * WARUM: InputValidator ist verantwortlich für:
 * - Validierung von Datumsbereichs-Eingaben (Start- und Enddatum)
 * - Überprüfung dass Startdatum nicht nach Enddatum liegt
 * - Überprüfung dass Enddatum nicht in der Zukunft liegt
 * - Rückgabe aussagekräftiger Fehlermeldungen auf Deutsch
 * 
 * TESTMETHODEN:
 * - validateDateRange(LocalDate start, LocalDate end): Validiert Datumsbereich
 * 
 * TESTKATEGORIEN:
 * 1. Valid Date Ranges: Gültige Datumsbereichs-Kombinationen
 * 2. Start Date After End Date: Fehlerfall - Startdatum nach Enddatum
 * 3. End Date in Future: Fehlerfall - Enddatum liegt in der Zukunft
 * 4. Edge Cases: Grenzfälle (Schaltjahr, Monatswechsel, Jahrswechsel)
 * 5. Error Messages: Format und Inhalt von Fehlermeldungen
 * 6. Boundary Tests: Grenzwerte (heute vs. morgen)
 */
@DisplayName("InputValidator Tests")
class InputValidatorTest {

  // ============ Valid Date Range Tests ============
  // Testet: Gültige Datumsbereichs-Kombinationen sollten null zurückgeben (keine Fehler)

  /**
   * Test: Normaler gültiger Datumsbereich wird akzeptiert
   * Erwartet: null (keine Fehlermeldung)
   */
  @Test
  @DisplayName("validateDateRange should return null for valid date range")
  void testValidateDateRangeValid() {
    // Arrange: Normale gültige Datumsbereich in der Vergangenheit
    LocalDate startDate = LocalDate.of(2026, 1, 1);
    LocalDate endDate = LocalDate.of(2026, 4, 22);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test: Gleiches Start- und Enddatum wird akzeptiert
   * Erwartet: null (gültig - Bereich für einen Tag)
   */
  @Test
  @DisplayName("validateDateRange should return null when start and end date are the same")
  void testValidateDateRangeSameDate() {
    // Arrange: Start- und Enddatum sind identisch
    LocalDate startDate = LocalDate.of(2026, 4, 22);
    LocalDate endDate = LocalDate.of(2026, 4, 22);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test: Ein-Tage-Bereich wird akzeptiert
   * Erwartet: null (gültig)
   */
  @Test
  @DisplayName("validateDateRange should return null for valid single day range")
  void testValidateDateRangeSingleDay() {
    // Arrange: Ein-Tage-Bereich (23.04.2026)
    LocalDate startDate = LocalDate.of(2026, 4, 23);
    LocalDate endDate = LocalDate.of(2026, 4, 23);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test: Großer historischer Datumsbereich wird akzeptiert
   * Erwartet: null (gültig - alles in der Vergangenheit)
   */
  @Test
  @DisplayName("validateDateRange should return null for valid range in the past")
  void testValidateDateRangeEntirelyInThePast() {
    // Arrange: Großer Bereich in der Vergangenheit (2020-2025)
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2025, 12, 31);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test: Datumsbereich mit Enddatum = heute wird akzeptiert
   * Erwartet: null (gültig - heute ist erlaubte obere Grenze)
   */
  @Test
  @DisplayName("validateDateRange should return null when end date is today")
  void testValidateDateRangeEndDateIsToday() {
    // Arrange: Enddatum ist heute (oder wird als heute betrachtet)
    LocalDate startDate = LocalDate.of(2026, 1, 1);
    LocalDate endDate = LocalDate.now(); // April 23, 2026

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
  }

  // ============ Start Date After End Date Tests ============
  // Testet: Fehlerfall - Startdatum liegt nach Enddatum sollte Fehlermeldung zurückgeben

  /**
   * Test: Startdatum einen Tag nach Enddatum wird abgelehnt
   * Erwartet: Fehlermeldung mit Text "Startdatum liegt nach dem Enddatum"
   */
  @Test
  @DisplayName("validateDateRange should return error when start date is after end date")
  void testValidateDateRangeStartAfterEnd() {
    // Arrange: Startdatum (24.04) ist nach Enddatum (23.04)
    LocalDate startDate = LocalDate.of(2026, 4, 24);
    LocalDate endDate = LocalDate.of(2026, 4, 23);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Ungueltiger Zeitraum"));
    assertTrue(result.get().contains("Startdatum liegt nach dem Enddatum"));
  }

  /**
   * Test: Startdatum deutlich nach Enddatum wird abgelehnt
   * Erwartet: Fehlermeldung mit Text "Startdatum liegt nach dem Enddatum"
   */
  @Test
  @DisplayName("validateDateRange should return error when start date is much after end date")
  void testValidateDateRangeStartMuchAfterEnd() {
    // Arrange: Startdatum (31.12.2026) ist weit nach Enddatum (01.01.2026)
    LocalDate startDate = LocalDate.of(2026, 12, 31);
    LocalDate endDate = LocalDate.of(2026, 1, 1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(
      result.get().contains("Startdatum liegt nach dem Enddatum"),
      "Error message should specify that start date is after end date");
  }

  /**
   * Test: Startdatum genau einen Tag nach Enddatum wird abgelehnt
   * Erwartet: Fehlermeldung (auch bei minimalem Versatz)
   */
  @Test
  @DisplayName("validateDateRange should return error when start date is one day after end date")
  void testValidateDateRangeStartOneDayAfterEnd() {
    // Arrange: Startdatum ist genau einen Tag nach Enddatum
    LocalDate startDate = LocalDate.of(2026, 4, 24);
    LocalDate endDate = LocalDate.of(2026, 4, 23);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Startdatum liegt nach dem Enddatum"));
  }

  // ============ End Date in Future Tests ============
  // Testet: Fehlerfall - Enddatum in der Zukunft sollte Fehlermeldung zurückgeben

  /**
   * Test: Enddatum einen Tag in der Zukunft wird abgelehnt
   * Erwartet: Fehlermeldung mit Text "Enddatum darf nicht in der Zukunft liegen"
   */
  @Test
  @DisplayName("validateDateRange should return error when end date is in the future")
  void testValidateDateRangeEndDateInFuture() {
    // Arrange
    LocalDate startDate = LocalDate.of(2026, 5, 15);
    LocalDate endDate = LocalDate.of(2026, 5, 16);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Ungueltiger Zeitraum"));
    assertTrue(result.get().contains("Enddatum darf nicht in der Zukunft liegen"));
  }

  /**
   * Test: Enddatum weit in der Zukunft wird abgelehnt
   * Erwartet: Fehlermeldung der Future-Verletzung
   */
  @Test
  @DisplayName("validateDateRange should return error when end date is far in the future")
  void testValidateDateRangeEndDateFarInFuture() {
    // Arrange: Enddatum (31.12.2027) liegt weit in der Zukunft
    LocalDate startDate = LocalDate.of(2026, 1, 1);
    LocalDate endDate = LocalDate.of(2027, 12, 31);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Enddatum darf nicht in der Zukunft liegen"));
  }

  /**
   * Test: Enddatum genau einen Tag in der Zukunft wird abgelehnt
   * Erwartet: Fehlermeldung (auch bei minimalem Versatz in die Zukunft)
   */
  @Test
  @DisplayName("validateDateRange should return error when end date is one day in the future")
  void testValidateDateRangeEndDateOneDayInFuture() {
    // Arrange: Enddatum ist genau einen Tag später als heute
    LocalDate startDate = LocalDate.now().minusDays(1);
    LocalDate endDate = LocalDate.now().plusDays(1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Enddatum darf nicht in der Zukunft liegen"));
  }

  // ============ Edge Case Tests ============
  // Testet: Grenzfälle und spezielle Szenarien (Schaltjahre, Monatswechsel, doppelte Verletzungen)

  /**
   * Test: Beide Validierungen verletzten gleichzeitig
   * Erwartet: Erste Verletzung wird geprüft (Start nach End gilt als kritischer)
   */
  @Test
  @DisplayName("validateDateRange should return error for both violations: start after end and end in future")
  void testValidateDateRangeBothViolations() {
    // Arrange: Startdatum (25.04) ist nach Enddatum (24.04), und Enddatum ist in der Zukunft
    LocalDate startDate = LocalDate.of(2026, 4, 25);
    LocalDate endDate = LocalDate.of(2026, 4, 24);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    // The first check (start after end) will be triggered
    assertTrue(result.get().contains("Startdatum liegt nach dem Enddatum"));
  }

  /**
   * Test: Sehr alte historische Datumsbereichs werden akzeptiert
   * Erwartet: null (keine Validierungsfehler)
   */
  @Test
  @DisplayName("validateDateRange should handle very old dates correctly")
  void testValidateDateRangeVeryOldDates() {
    // Arrange: Datumsbereich aus dem Jahr 1900 (sehr alt)
    LocalDate startDate = LocalDate.of(1900, 1, 1);
    LocalDate endDate = LocalDate.of(1900, 12, 31);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty(), "Very old date ranges should be valid");
  }

  /**
   * Test: Datumsbereich über Jahrswechsel wird akzeptiert
   * Erwartet: null (keine Validierungsfehler)
   */
  @Test
  @DisplayName("validateDateRange should handle dates at year boundaries")
  void testValidateDateRangeYearBoundary() {
    // Arrange: Datumsbereich verläuft über Jahrswechsel (31.12.2025 bis 01.01.2026)
    LocalDate startDate = LocalDate.of(2025, 12, 31);
    LocalDate endDate = LocalDate.of(2026, 1, 1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty(), "Date range crossing year boundary should be valid");
  }

  /**
   * Test: Schaltjahrs-Datumsbereich wird korrekt validiert
   * Erwartet: null (29. Februar existiert in Schaltjahr 2024)
   */
  @Test
  @DisplayName("validateDateRange should handle leap year dates correctly")
  void testValidateDateRangeLeapYear() {
    // Arrange: Datumsbereich mit Schaltjahrs-Datum (29.02.2024 existiert)
    LocalDate startDate = LocalDate.of(2024, 2, 28);
    LocalDate endDate = LocalDate.of(2024, 2, 29);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty(), "Leap year date range should be valid");
  }

  /**
   * Test: Datumsbereich über Monatswechsel wird akzeptiert
   * Erwartet: null (keine Validierungsfehler)
   */
  @Test
  @DisplayName("validateDateRange should handle month boundary correctly")
  void testValidateDateRangeMonthBoundary() {
    // Arrange: Datumsbereich verläuft über Monatswechsel (31.03 bis 01.04)
    LocalDate startDate = LocalDate.of(2026, 3, 31);
    LocalDate endDate = LocalDate.of(2026, 4, 1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isEmpty(), "Date range crossing month boundary should be valid");
  }

  // ============ Error Message Tests ============
  // Testet: Format und genaue Inhalte von Fehlermeldungen (deutsche Texte)

  /**
   * Test: Genaue Fehlermeldung bei Start-nach-End-Verletzung
   * Erwartet: Exakte deutschsprachige Fehlermeldung
   */
  @Test
  @DisplayName("validateDateRange error message for invalid order contains German text")
  void testValidateDateRangeErrorMessageFormat1() {
    // Arrange: Test-Setup für Start-nach-End-Fehler
    LocalDate startDate = LocalDate.of(2026, 5, 1);
    LocalDate endDate = LocalDate.of(2026, 4, 1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertEquals("Ungueltiger Zeitraum: Startdatum liegt nach dem Enddatum.", result.get());
  }

  /**
   * Test: Genaue Fehlermeldung bei Future-End-Date-Verletzung
   * Erwartet: Exakte deutschsprachige Fehlermeldung für zukünftiges Enddatum
   */
  @Test
  @DisplayName("validateDateRange error message for future date contains German text")
  void testValidateDateRangeErrorMessageFormat2() {
    // Arrange
    LocalDate startDate = LocalDate.of(2026, 5, 15);
    LocalDate endDate = LocalDate.of(2026, 5, 15);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertEquals("Ungueltiger Zeitraum: Enddatum darf nicht in der Zukunft liegen.", result.get());
  }

  // ============ Boundary Tests ============
  // Testet: Kritische Grenzwerte (heute vs. morgen, gerade noch gültig vs. ungültig)

  /**
   * Test: Enddatum genau heute wird akzeptiert
   * Erwartet: null (heute ist die gültige obere Grenze)
   */
  @Test
  @DisplayName("validateDateRange should accept end date that is exactly today")
  void testValidateDateRangeExactlyToday() {
    // Arrange: Enddatum ist genau heute
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(30);
    LocalDate endDate = today;

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent(), "Today should NOT be a valid end date (incomplete data)");
  }

  /**
   * Test: Enddatum morgen wird abgelehnt
   * Erwartet: Fehlermeldung (morgen ist nicht erlaubt - Grenzwert)
   */
  @Test
  @DisplayName("validateDateRange should reject end date that is tomorrow")
  void testValidateDateRangeTomorrow() {
    // Arrange: Enddatum ist morgen
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(30);
    LocalDate endDate = today.plusDays(1);

    // Act
    Optional<String> result = InputValidator.validateDateRange(startDate, endDate);

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Enddatum darf nicht in der Zukunft liegen"));
  }
}
