package de.hsh.hws.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test-Klasse für DateInputManager.
 * 
 * GETESTETE KLASSE: DateInputManager
 * 
 * WARUM: DateInputManager ist verantwortlich für:
 * - Benutzereingabe von Datumsangaben (Start- und Enddatum) entgegennehmen
 * - Benutzereingaben validieren und in LocalDate-Objekte konvertieren
 * - Aussagekräftige Fehlermeldungen bei ungültigen Eingaben
 * 
 * TESTMETHODEN:
 * 1. readStartDateInput: Liest Startdatum vom Benutzer ein
 * 2. readEndDateInput: Liest Enddatum vom Benutzer ein
 * 3. parseStartDate: Konvertiert String in LocalDate (Startdatum)
 * 4. parseEndDate: Konvertiert String in LocalDate (Enddatum)
 * 
 * TESTTECHNIKEN:
 * - Mocking: Scanner wird gemockt, um Benutzereingaben zu simulieren
 * - Output-Capture: System.out wird abgefangen, um Prompts zu überprüfen
 * - Exception-Testing: Wird überprüft, dass Fehler korrekt geworfen werden
 */
@DisplayName("DateInputManager Tests")
class DateInputManagerTest {

  @Mock private Scanner scannerMock;

  private DateInputManager dateInputManager;
  private ByteArrayOutputStream outputCapture;
  private PrintStream printStream;

// initialisiert die Mocks und den DateInputManager vor jedem Test, und fängt System.out ab,
//  um die Konsolenausgabe zu überprüfen
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    outputCapture = new ByteArrayOutputStream();
    printStream = new PrintStream(outputCapture);
    dateInputManager = new DateInputManager(scannerMock, printStream);
  }
// stellt System.out nach jedem Test wieder her, um Seiteneffekte zu vermeiden
  @AfterEach
  void tearDown() {
    printStream.close();
  }

  
  // ============ readStartDateInput Tests ============
  // Testet: Die readStartDateInput() Methode liest korrekt Benutzereingaben ein und zeigt Prompts

  /**
   * Test: readStartDateInput gibt korrekt Benutzereingabe zurück
   * Erwartet: Input wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("readStartDateInput should return user input when valid")
  void testReadStartDateInputSuccess() {
    // Arrange: Simuliere Scanner-Eingabe mit gültigem Datumsstring
    String expectedInput = "2026-04-23";
    when(scannerMock.nextLine()).thenReturn(expectedInput);

    // Act: Rufe readStartDateInput auf
    String result = dateInputManager.readStartDateInput();

    // Assert: Überprüfe dass die Eingabe korrekt zurückgegeben wird
    assertEquals(expectedInput, result);
    verify(scannerMock).nextLine();
    assertTrue(outputCapture.toString().contains("Start date (yyyy-mm-dd):"));
  }

  /**
   * Test: readStartDateInput zeigt korrekte Benutzer-Aufforderung an
   * Erwartet: Der Prompt wird auf der Konsole ausgegeben
   */
  @Test
  @DisplayName("readStartDateInput should print correct prompt")
  void testReadStartDateInputPrintsCorrectPrompt() {
    // Arrange: Simuliere Scanner-Eingabe
    when(scannerMock.nextLine()).thenReturn("2026-04-23");

    // Act: Rufe readStartDateInput auf
    dateInputManager.readStartDateInput();

    // Assert: Überprüfe dass der korrekte Prompt ausgegeben wurde
    String output = outputCapture.toString();
    assertTrue(output.contains("Start date (yyyy-mm-dd):"));
  }

  /**
   * Test: readStartDateInput wirft Exception, wenn Scanner Fehler wirft
   * Erwartet: RuntimeException mit aussagekräftiger Fehlermeldung
   */
  @Test
  @DisplayName("readStartDateInput should throw RuntimeException on Scanner error")
  void testReadStartDateInputThrowsExceptionOnError() {
    // Arrange: Simuliere Scanner-Fehler mit Exception
    when(scannerMock.nextLine()).thenThrow(new RuntimeException("Scanner error"));

    // Act & Assert: Überprüfe dass RuntimeException geworfen wird mit richtiger Nachricht
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.readStartDateInput());
    assertTrue(exception.getMessage().contains("Fehler beim Lesen des Startdatums:"));
  }

  // ============ readEndDateInput Tests ============
  // Testet: Die readEndDateInput() Methode liest korrekt Benutzereingaben ein und zeigt Prompts

  /**
   * Test: readEndDateInput gibt korrekt Benutzereingabe zurück
   * Erwartet: Input wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("readEndDateInput should return user input when valid")
  void testReadEndDateInputSuccess() {
    // Arrange: Simuliere Scanner-Eingabe mit gültigem Datumsstring
    String expectedInput = "2026-05-23";
    when(scannerMock.nextLine()).thenReturn(expectedInput);

    // Act: Rufe readEndDateInput auf
    String result = dateInputManager.readEndDateInput();

    // Assert: Überprüfe dass die Eingabe korrekt zurückgegeben wird
    assertEquals(expectedInput, result);
    verify(scannerMock).nextLine();
    assertTrue(outputCapture.toString().contains("End date (yyyy-mm-dd):"));
  }

  /**
   * Test: readEndDateInput zeigt korrekte Benutzer-Aufforderung an
   * Erwartet: Der Prompt wird auf der Konsole ausgegeben
   */
  @Test
  @DisplayName("readEndDateInput should print correct prompt")
  void testReadEndDateInputPrintsCorrectPrompt() {
    // Arrange: Simuliere Scanner-Eingabe
    when(scannerMock.nextLine()).thenReturn("2026-05-23");

    // Act: Rufe readEndDateInput auf
    dateInputManager.readEndDateInput();

    // Assert: Überprüfe dass der korrekte Prompt ausgegeben wurde
    String output = outputCapture.toString();
    assertTrue(output.contains("End date (yyyy-mm-dd):"));
  }

  /**
   * Test: readEndDateInput wirft Exception, wenn Scanner Fehler wirft
   * Erwartet: RuntimeException mit aussagekräftiger Fehlermeldung
   */
  @Test
  @DisplayName("readEndDateInput should throw RuntimeException on Scanner error")
  void testReadEndDateInputThrowsExceptionOnError() {
    // Arrange: Simuliere Scanner-Fehler mit Exception
    when(scannerMock.nextLine()).thenThrow(new RuntimeException("Scanner error"));

    // Act & Assert: Überprüfe dass RuntimeException geworfen wird mit richtiger Nachricht
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.readEndDateInput());
    assertTrue(exception.getMessage().contains("Fehler beim Lesen des Enddatums:"));
  }

  // ============ parseStartDate Tests ============
  // Testet: Die parseStartDate() Methode konvertiert String-Eingaben korrekt zu LocalDate-Objekten
  // Wichtig: Validierung von Format, gültigen Datumsangaben, Edge Cases (Schaltjahre, ungültige Tage)

  /**
   * Test: parseStartDate konvertiert gültiges Datumsformat korrekt
   * Erwartet: String "2026-04-23" wird zu LocalDate(2026, 4, 23)
   */
  @Test
  @DisplayName("parseStartDate should parse valid date string correctly")
  void testParseStartDateValid() {
    // Arrange: Vorbereitung mit gültigem Datumsformat
    String validDate = "2026-04-23";
    LocalDate expected = LocalDate.of(2026, 4, 23);

    // Act: Konvertiere String zu LocalDate
    LocalDate result = dateInputManager.parseStartDate(validDate);

    // Assert: Überprüfe dass das Datum korrekt geparst wurde
    assertEquals(expected, result);
  }

  /**
   * Test: parseStartDate wirft Exception bei ungültigem Format
   * Erwartet: RuntimeException mit Hinweis auf korrektes Format (yyyy-mm-dd)
   */
  @Test
  @DisplayName("parseStartDate should throw RuntimeException for invalid format")
  void testParseStartDateInvalidFormat() {
    // Arrange: Datumsstring mit falschem Format (dd-mm-yyyy statt yyyy-mm-dd)
    String invalidDate = "23-04-2026";

    // Act & Assert: Überprüfe dass Exception mit Formathinweis geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseStartDate(invalidDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Startdatum"));
    assertTrue(exception.getMessage().contains("yyyy-mm-dd"));
  }

  /**
   * Test: parseStartDate wirft Exception bei ungültigem Datum
   * Erwartet: RuntimeException (z.B. 30. Februar existiert nicht)
   */
  @Test
  @DisplayName("parseStartDate should throw RuntimeException for invalid date")
  void testParseStartDateInvalidDate() {
    // Arrange: Ungültiges Datum (30. Februar existiert nicht)
    String invalidDate = "2026-02-30";

    // Act & Assert: Überprüfe dass Exception bei zeitlich unmöglichem Datum geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseStartDate(invalidDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Startdatum"));
  }

  /**
   * Test: parseStartDate wirft Exception bei leerem String
   * Erwartet: RuntimeException
   */
  @Test
  @DisplayName("parseStartDate should throw RuntimeException for empty string")
  void testParseStartDateEmptyString() {
    // Arrange: Leerer String
    String emptyDate = "";

    // Act & Assert: Überprüfe dass Exception bei leerer Eingabe geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseStartDate(emptyDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Startdatum"));
  }

  /**
   * Test: parseStartDate wirft Exception bei null-Wert
   * Erwartet: RuntimeException
   */
  @Test
  @DisplayName("parseStartDate should throw RuntimeException for null")
  void testParseStartDateNull() {
    // Act & Assert: Überprüfe dass Exception bei null geworfen wird
    assertThrows(RuntimeException.class, () -> dateInputManager.parseStartDate(null));
  }

  /**
   * Test: parseStartDate konvertiert Schaltjahr-Datum korrekt
   * Erwartet: 29. Februar 2024 wird korrekt erkannt und geparst (2024 ist Schaltjahr)
   */
  @Test
  @DisplayName("parseStartDate should parse leap year date correctly")
  void testParseStartDateLeapYear() {
    // Arrange: Vorbereitung mit Schaltjahres-Datum (29.02.2024 existiert)
    String leapYearDate = "2024-02-29";
    LocalDate expected = LocalDate.of(2024, 2, 29);

    // Act: Konvertiere Schaltjahres-Datum
    LocalDate result = dateInputManager.parseStartDate(leapYearDate);

    // Assert: Überprüfe dass Schaltjahres-Datum korrekt geparst wird
    assertEquals(expected, result);
  }

  // ============ parseEndDate Tests ============
  // Testet: Die parseEndDate() Methode konvertiert String-Eingaben korrekt zu LocalDate-Objekten
  // Analog zu parseStartDate Tests aber für Enddatum-Validierung

  /**
   * Test: parseEndDate konvertiert gültiges Datumsformat korrekt
   * Erwartet: String "2026-05-23" wird zu LocalDate(2026, 5, 23)
   */
  @Test
  @DisplayName("parseEndDate should parse valid date string correctly")
  void testParseEndDateValid() {
    // Arrange: Vorbereitung mit gültigem Datumsformat
    String validDate = "2026-05-23";
    LocalDate expected = LocalDate.of(2026, 5, 23);

    // Act: Konvertiere String zu LocalDate
    LocalDate result = dateInputManager.parseEndDate(validDate);

    // Assert: Überprüfe dass das Datum korrekt geparst wurde
    assertEquals(expected, result);
  }

  /**
   * Test: parseEndDate wirft Exception bei ungültigem Format
   * Erwartet: RuntimeException mit Hinweis auf korrektes Format (yyyy-mm-dd)
   */
  @Test
  @DisplayName("parseEndDate should throw RuntimeException for invalid format")
  void testParseEndDateInvalidFormat() {
    // Arrange: Datumsstring mit falschem Format (dd-mm-yyyy statt yyyy-mm-dd)
    String invalidDate = "23-05-2026";

    // Act & Assert: Überprüfe dass Exception mit Formathinweis geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseEndDate(invalidDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Enddatum"));
    assertTrue(exception.getMessage().contains("yyyy-mm-dd"));
  }

  /**
   * Test: parseEndDate wirft Exception bei ungültigem Datum
   * Erwartet: RuntimeException (z.B. 31. April existiert nicht)
   */
  @Test
  @DisplayName("parseEndDate should throw RuntimeException for invalid date")
  void testParseEndDateInvalidDate() {
    // Arrange: Ungültiges Datum (31. April existiert nicht)
    String invalidDate = "2026-04-31";

    // Act & Assert: Überprüfe dass Exception bei zeitlich unmöglichem Datum geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseEndDate(invalidDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Enddatum"));
  }

  /**
   * Test: parseEndDate wirft Exception bei leerem String
   * Erwartet: RuntimeException
   */
  @Test
  @DisplayName("parseEndDate should throw RuntimeException for empty string")
  void testParseEndDateEmptyString() {
    // Arrange: Leerer String
    String emptyDate = "";

    // Act & Assert: Überprüfe dass Exception bei leerer Eingabe geworfen wird
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> dateInputManager.parseEndDate(emptyDate));
    assertTrue(exception.getMessage().contains("Ungueltiges Enddatum"));
  }

  /**
   * Test: parseEndDate wirft Exception bei null-Wert
   * Erwartet: RuntimeException
   */
  @Test
  @DisplayName("parseEndDate should throw RuntimeException for null")
  void testParseEndDateNull() {
    // Act & Assert: Überprüfe dass Exception bei null geworfen wird
    assertThrows(RuntimeException.class, () -> dateInputManager.parseEndDate(null));
  }

  /**
   * Test: parseEndDate konvertiert Schaltjahr-Datum korrekt
   * Erwartet: 29. Februar 2024 wird korrekt erkannt und geparst (2024 ist Schaltjahr)
   */
  @Test
  @DisplayName("parseEndDate should parse leap year date correctly")
  void testParseEndDateLeapYear() {
    // Arrange: Vorbereitung mit Schaltjahres-Datum (29.02.2024 existiert)
    String leapYearDate = "2024-02-29";
    LocalDate expected = LocalDate.of(2024, 2, 29);

    // Act: Konvertiere Schaltjahres-Datum
    LocalDate result = dateInputManager.parseEndDate(leapYearDate);

    // Assert: Überprüfe dass Schaltjahres-Datum korrekt geparst wird
    assertEquals(expected, result);
  }

  // ============ Integration-like Tests ============
  // Testet: Kombinierte Workflows von Eingabe und Verarbeitung (read + parse zusammen)
  // Wichtig: Überprüft, dass Read- und Parse-Methoden harmonisch zusammenarbeiten

  /**
   * Test: Kompletter Workflow Startdatum (Read + Parse)
   * Erwartet: Benutzereingabe wird gelesen und korrekt zu LocalDate geparst
   */
  @Test
  @DisplayName("should handle complete workflow: read and parse start date")
  void testCompleteWorkflowStartDate() {
    // Arrange: Simuliere Benutzereingabe und erwartetes Ergebnis
    String userInput = "2026-04-23";
    LocalDate expected = LocalDate.of(2026, 4, 23);
    when(scannerMock.nextLine()).thenReturn(userInput);

    // Act: Führe kompletten Workflow aus (read + parse)
    String input = dateInputManager.readStartDateInput();
    LocalDate parsedDate = dateInputManager.parseStartDate(input);

    // Assert: Überprüfe dass beide Operationen zusammen das korrekte Ergebnis liefern
    assertEquals(expected, parsedDate);
    verify(scannerMock).nextLine();
  }

  /**
   * Test: Kompletter Workflow Enddatum (Read + Parse)
   * Erwartet: Benutzereingabe wird gelesen und korrekt zu LocalDate geparst
   */
  @Test
  @DisplayName("should handle complete workflow: read and parse end date")
  void testCompleteWorkflowEndDate() {
    // Arrange: Simuliere Benutzereingabe und erwartetes Ergebnis
    String userInput = "2026-05-23";
    LocalDate expected = LocalDate.of(2026, 5, 23);
    when(scannerMock.nextLine()).thenReturn(userInput);

    // Act: Führe kompletten Workflow aus (read + parse)
    String input = dateInputManager.readEndDateInput();
    LocalDate parsedDate = dateInputManager.parseEndDate(input);

    // Assert: Überprüfe dass beide Operationen zusammen das korrekte Ergebnis liefern
    assertEquals(expected, parsedDate);
    verify(scannerMock).nextLine();
  }
}
