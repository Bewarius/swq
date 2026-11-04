package de.hsh.hws.stats;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test-Klasse für WeatherStatisticsPrinter.
 *
 * GETESTETE KLASSE: WeatherStatisticsPrinter
 *
 * WARUM: WeatherStatisticsPrinter ist verantwortlich für:
 * - Formatierung und Ausgabe von Wetterstatistiken auf der Konsole
 * - Benutzerfreundliche Darstellung aller statistischen Werte (Min, Max, Mean, Median, Cold%, Hot%)
 * - Korrekte Formatierung von Zahlen (eine Dezimalstelle, Einheiten °C und %)
 * - Behandlung von Sonderfällen (NaN-Werte, negative Temperaturen)
 * - Konsistente Ausgabe-Formatierung für CLI-Anwendung
 *
 * TESTMETHODEN:
 * - print(WeatherStatistics stats): Gibt formatierte Statistiken auf System.out aus
 *
 * TESTTECHNIKEN:
 * - Output-Capture: System.out wird abgefangen und analysiert
 * - String-Assertions: Überprüfung der Ausgabe-Inhalte und -Formate
 * - Edge-Case-Testing: NaN, negative Werte, Grenzwerte (0%, 100%)
 * - Format-Testing: Dezimalstellen, Einheiten, Zeilenanzahl
 */
@DisplayName("WeatherStatisticsPrinter Tests")
class WeatherStatisticsPrinterTest {

  private WeatherStatisticsPrinter printer;
  private ByteArrayOutputStream outputStream;
  private PrintStream printStream;
  private PrintStream originalOut;

  @BeforeEach
  void setUp() {
    // Umleiten von System.out für Testing - ermöglicht Analyse der Konsolen-Ausgabe
    originalOut = System.out;
    outputStream = new ByteArrayOutputStream();
    printStream = new PrintStream(outputStream);
    printer = new WeatherStatisticsPrinter(printStream);
  }

  @AfterEach
  void tearDown() {
    printStream.close();
  }

  /**
   * Test: print() gibt alle erforderlichen Felder aus
   * Erwartet: Alle 6 statistischen Werte werden in der Ausgabe erwähnt
   */
  @Test
  @DisplayName("Printer sollte alle Felder ausgeben")
  void testPrintAllFields() {
    // Arrange: Erstelle Statistics-Objekt mit allen Werten
    WeatherStatistics stats = new WeatherStatistics(10.5, 25.3, 17.9, 18.1, 15.2, 5.8);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass alle Feldnamen in der Ausgabe enthalten sind
    assertTrue(output.contains("Min. Temperature"));
    assertTrue(output.contains("Max. Temperature"));
    assertTrue(output.contains("Mean Temperature"));
    assertTrue(output.contains("Median Temperature"));
    assertTrue(output.contains("Cold"));
    assertTrue(output.contains("Hot"));
  }

  /**
   * Test: print() formatiert Werte korrekt mit einer Dezimalstelle
   * Erwartet: Zahlen werden auf eine Dezimalstelle gerundet
   */
  @Test
  @DisplayName("Printer sollte Werte mit einer Dezimalstelle formatieren")
  void testPrintFormatting() {
    // Arrange: Erstelle Statistics mit Werten die gerundet werden müssen
    WeatherStatistics stats = new WeatherStatistics(10.555, 25.999, 17.123, 18.456, 15.234, 5.789);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe korrekte Rundung auf eine Dezimalstelle
    assertTrue(output.contains("10.6")); // Aufgerundet
    assertTrue(output.contains("26.0")); // Aufgerundet
    assertTrue(output.contains("17.1")); // Abgerundet
  }

  /**
   * Test: print() behandelt NaN-Werte korrekt
   * Erwartet: NaN-Werte werden als "NaN" in der Ausgabe dargestellt
   */
  @Test
  @DisplayName("Printer sollte NaN-Werte als 'NaN' ausgeben")
  void testPrintNaN() {
    // Arrange: Erstelle Statistics mit NaN-Werten (z.B. bei fehlenden Daten)
    WeatherStatistics stats = new WeatherStatistics(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass NaN-Werte korrekt als "NaN" ausgegeben werden
    assertTrue(output.contains("NaN"));
  }

  /**
   * Test: print() zeigt Celsius-Einheit korrekt an
   * Erwartet: Temperaturwerte werden mit °C angezeigt
   */
  @Test
  @DisplayName("Printer sollte die Celsius-Einheit ausgeben")
  void testPrintCelsiusUnit() {
    // Arrange: Erstelle Statistics mit normalen Temperaturwerten
    WeatherStatistics stats = new WeatherStatistics(0.0, 30.0, 15.0, 15.0, 0.0, 100.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass Celsius-Einheit angezeigt wird
    assertTrue(output.contains("°C"));
  }

  /**
   * Test: print() zeigt Prozentzeichen korrekt an
   * Erwartet: Prozentwerte werden mit % angezeigt (für Cold und Hot)
   */
  @Test
  @DisplayName("Printer sollte die Prozentzeichen ausgeben")
  void testPrintPercentageUnit() {
    // Arrange: Erstelle Statistics mit Prozentwerten
    WeatherStatistics stats = new WeatherStatistics(0.0, 30.0, 15.0, 15.0, 50.0, 50.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass genau 2 Prozentzeichen vorhanden sind (für Cold und Hot)
    int percentCount = output.split("%").length - 1;
    assertEquals(2, percentCount); // 2 Prozent-Zeichen für Cold und Hot
  }

  /**
   * Test: print() gibt negative Temperaturen korrekt aus
   * Erwartet: Negative Werte werden mit Minuszeichen dargestellt
   */
  @Test
  @DisplayName("Printer sollte negative Temperaturen korrekt ausgeben")
  void testPrintNegativeTemperatures() {
    // Arrange: Erstelle Statistics mit negativen Temperaturen (Winterwerte)
    WeatherStatistics stats = new WeatherStatistics(-15.5, -5.3, -10.4, -10.2, 100.0, 0.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass negative Werte korrekt ausgegeben werden
    assertTrue(output.contains("-15.5"));
    assertTrue(output.contains("-5.3"));
    assertTrue(output.contains("-10.4"));
  }

  /**
   * Test: print() gibt hohe Temperaturen korrekt aus
   * Erwartet: Hohe positive Werte werden korrekt dargestellt
   */
  @Test
  @DisplayName("Printer sollte hohe Temperaturen korrekt ausgeben")
  void testPrintHighTemperatures() {
    // Arrange: Erstelle Statistics mit hohen Temperaturen (Sommerwerte)
    WeatherStatistics stats = new WeatherStatistics(35.5, 45.8, 40.2, 40.5, 0.0, 100.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass hohe Werte korrekt ausgegeben werden
    assertTrue(output.contains("35.5"));
    assertTrue(output.contains("45.8"));
    assertTrue(output.contains("40.2"));
  }

  /**
   * Test: print() formatiert 0-Grad korrekt
   * Erwartet: Null-Werte werden als "0.0" dargestellt
   */
  @Test
  @DisplayName("Printer sollte 0-Grad korrekt formatieren")
  void testPrintZeroDegree() {
    // Arrange: Erstelle Statistics mit allen Null-Werten
    WeatherStatistics stats = new WeatherStatistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass 0.0 korrekt formatiert wird
    assertTrue(output.contains("0.0"));
  }

  /**
   * Test: print() gibt maximale Prozentwerte (100%) korrekt aus
   * Erwartet: 100.0% wird korrekt dargestellt
   */
  @Test
  @DisplayName("Printer sollte maximale Prozentagen (100%) ausgeben")
  void testPrintMaxPercentage() {
    // Arrange: Erstelle Statistics mit 100% Cold und Hot
    WeatherStatistics stats = new WeatherStatistics(-10.0, 40.0, 15.0, 15.0, 100.0, 100.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass 100.0% korrekt ausgegeben wird
    assertTrue(output.contains("100.0"));
  }

  /**
   * Test: print() gibt minimale Prozentwerte (0%) korrekt aus
   * Erwartet: 0.0% wird korrekt dargestellt
   */
  @Test
  @DisplayName("Printer sollte minimale Prozentagen (0%) ausgeben")
  void testPrintMinPercentage() {
    // Arrange: Erstelle Statistics mit 0% Cold und Hot
    WeatherStatistics stats = new WeatherStatistics(5.0, 25.0, 15.0, 15.0, 0.0, 0.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass 0.0% korrekt ausgegeben wird
    assertTrue(output.contains("0.0"));
  }

  /**
   * Test: print() gibt die korrekte Anzahl von Ausgabezeilen
   * Erwartet: Genau 6 Zeilen (eine für jeden statistischen Wert)
   */
  @Test
  @DisplayName("Printer sollte die richtige Anzahl von Zeilen ausgeben")
  void testPrintLineCount() {
    // Arrange: Erstelle Statistics mit normalen Werten
    WeatherStatistics stats = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);

    // Act: Gib Statistiken aus und erfasse Ausgabe
    printer.print(stats);
    String output = outputStream.toString();

    // Assert: Überprüfe dass genau 6 Zeilen ausgegeben werden (jede println() erzeugt eine Zeile)
    long lineCount = output.lines().count();
    assertEquals(6, lineCount); // 6 Ausgabezeilen
  }
}
