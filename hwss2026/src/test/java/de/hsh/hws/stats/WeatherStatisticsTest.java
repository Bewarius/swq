package de.hsh.hws.stats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test-Klasse für WeatherStatistics Record.
 *
 * GETESTETE KLASSE: WeatherStatistics
 *
 * WARUM: WeatherStatistics ist ein Java Record (Immutable Data Class) und:
 * - Speichert alle berechneten statistischen Werte (Min, Max, Mean, Median, Cold%, Hot%)
 * - Ist das zentrale Datenobjekt für die Kommunikation zwischen Calculator und Printer
 * - Muss korrekt erstellt werden und alle Werte korrekt bereitstellen
 * - Implementiert automatisch equals(), hashCode(), toString() (Record-Features)
 * - Ist immutable (alle Felder final) - wichtig für Thread-Safety und Datenintegrität
 *
 * TESTMETHODEN:
 * - Konstruktor: Erstellt WeatherStatistics-Objekte mit 6 Werten
 * - Getter-Methoden: min(), max(), mean(), median(), coldPercentage(), hotPercentage()
 * - equals(), hashCode(), toString(): Automatisch generierte Record-Methoden
 *
 * TESTTECHNIKEN:
 * - Constructor-Testing: Überprüfung korrekter Objekterstellung
 * - Getter-Testing: Überprüfung korrekter Wert-Rückgabe
 * - Equality-Testing: Überprüfung von equals() und hashCode()
 * - String-Representation: Überprüfung von toString()
 * - Edge-Case-Testing: NaN-Werte, Grenzwerte, Validierungen
 */
@DisplayName("WeatherStatistics Record Tests")
class WeatherStatisticsTest {

  private WeatherStatistics stats;

  @BeforeEach
  void setUp() {
    // Erstelle Standard-Testobjekt für die meisten Tests
    stats = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);
  }

  /**
   * Test: WeatherStatistics-Konstruktor erstellt Objekt korrekt
   * Erwartet: Alle 6 Werte werden korrekt gespeichert und können abgerufen werden
   */
  @Test
  @DisplayName("WeatherStatistics sollte korrekt erstellt werden")
  void testWeatherStatisticsCreation() {
    // Arrange & Act: Erstelle neues WeatherStatistics-Objekt mit allen Werten
    WeatherStatistics stats = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);

    // Assert: Überprüfe dass alle Werte korrekt gespeichert wurden
    assertNotNull(stats);
    assertEquals(10.0, stats.min());
    assertEquals(25.0, stats.max());
    assertEquals(17.5, stats.mean());
    assertEquals(18.0, stats.median());
    assertEquals(15.0, stats.coldPercentage());
    assertEquals(5.0, stats.hotPercentage());
  }

  /**
   * Test: min() Getter gibt Minimum-Wert korrekt zurück
   * Erwartet: Der gespeicherte Min-Wert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Min-Wert sollte korrekt abgerufen werden")
  void testGetMin() {
    // Act & Assert: Überprüfe dass min() den korrekten Wert zurückgibt
    assertEquals(10.0, stats.min());
  }

  /**
   * Test: max() Getter gibt Maximum-Wert korrekt zurück
   * Erwartet: Der gespeicherte Max-Wert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Max-Wert sollte korrekt abgerufen werden")
  void testGetMax() {
    // Act & Assert: Überprüfe dass max() den korrekten Wert zurückgibt
    assertEquals(25.0, stats.max());
  }

  /**
   * Test: mean() Getter gibt Mittelwert korrekt zurück
   * Erwartet: Der gespeicherte Mean-Wert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Mean-Wert sollte korrekt abgerufen werden")
  void testGetMean() {
    // Act & Assert: Überprüfe dass mean() den korrekten Wert zurückgibt
    assertEquals(17.5, stats.mean());
  }

  /**
   * Test: median() Getter gibt Median-Wert korrekt zurück
   * Erwartet: Der gespeicherte Median-Wert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Median-Wert sollte korrekt abgerufen werden")
  void testGetMedian() {
    // Act & Assert: Überprüfe dass median() den korrekten Wert zurückgibt
    assertEquals(18.0, stats.median());
  }

  /**
   * Test: coldPercentage() Getter gibt Cold-Prozentwert korrekt zurück
   * Erwartet: Der gespeicherte Cold-Prozentwert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Cold-Prozentage sollte korrekt abgerufen werden")
  void testGetColdPercentage() {
    // Act & Assert: Überprüfe dass coldPercentage() den korrekten Wert zurückgibt
    assertEquals(15.0, stats.coldPercentage());
  }

  /**
   * Test: hotPercentage() Getter gibt Hot-Prozentwert korrekt zurück
   * Erwartet: Der gespeicherte Hot-Prozentwert wird unverändert zurückgegeben
   */
  @Test
  @DisplayName("Hot-Prozentage sollte korrekt abgerufen werden")
  void testGetHotPercentage() {
    // Act & Assert: Überprüfe dass hotPercentage() den korrekten Wert zurückgibt
    assertEquals(5.0, stats.hotPercentage());
  }

  /**
   * Test: equals() erkennt identische WeatherStatistics-Objekte
   * Erwartet: Zwei Objekte mit gleichen Werten sind equal
   */
  @Test
  @DisplayName("Zwei gleiche WeatherStatistics sollten equal sein")
  void testEquality() {
    // Arrange: Erstelle zwei identische WeatherStatistics-Objekte
    WeatherStatistics stats1 = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);
    WeatherStatistics stats2 = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);

    // Act & Assert: Überprüfe dass equals() true zurückgibt für identische Objekte
    assertEquals(stats1, stats2);
  }

  /**
   * Test: equals() erkennt unterschiedliche WeatherStatistics-Objekte
   * Erwartet: Zwei Objekte mit unterschiedlichen Werten sind nicht equal
   */
  @Test
  @DisplayName("Unterschiedliche WeatherStatistics sollten nicht equal sein")
  void testInequality() {
    // Arrange: Erstelle zwei WeatherStatistics-Objekte mit einem unterschiedlichen Wert
    WeatherStatistics stats1 = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);
    WeatherStatistics stats2 = new WeatherStatistics(10.0, 26.0, 17.5, 18.0, 15.0, 5.0);

    // Act & Assert: Überprüfe dass equals() false zurückgibt für unterschiedliche Objekte
    assertNotEquals(stats1, stats2);
  }

  /**
   * Test: hashCode() ist konsistent für gleiche Objekte
   * Erwartet: Gleiche Objekte haben gleichen HashCode (equals-hashCode-Kontrakt)
   */
  @Test
  @DisplayName("Gleiche WeatherStatistics sollten gleichen HashCode haben")
  void testHashCode() {
    // Arrange: Erstelle zwei identische WeatherStatistics-Objekte
    WeatherStatistics stats1 = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);
    WeatherStatistics stats2 = new WeatherStatistics(10.0, 25.0, 17.5, 18.0, 15.0, 5.0);

    // Act & Assert: Überprüfe dass hashCode() für gleiche Objekte gleich ist
    assertEquals(stats1.hashCode(), stats2.hashCode());
  }

  /**
   * Test: toString() enthält alle Werte in lesbarer Form
   * Erwartet: String-Repräsentation enthält alle 6 statistischen Werte
   */
  @Test
  @DisplayName("toString() sollte alle Werte enthalten")
  void testToString() {
    // Act: Rufe toString() auf dem Testobjekt auf
    String result = stats.toString();

    // Assert: Überprüfe dass alle Werte in der String-Repräsentation enthalten sind
    assertNotNull(result);
    assertTrue(result.contains("10.0"));
    assertTrue(result.contains("25.0"));
    assertTrue(result.contains("17.5"));
    assertTrue(result.contains("18.0"));
    assertTrue(result.contains("15.0"));
    assertTrue(result.contains("5.0"));
  }

  /**
   * Test: WeatherStatistics kann mit NaN-Werten erstellt werden
   * Erwartet: NaN-Werte werden korrekt gespeichert und können abgerufen werden
   */
  @Test
  @DisplayName("WeatherStatistics mit NaN-Werten sollte erstellt werden")
  void testWeatherStatisticsWithNaN() {
    // Arrange & Act: Erstelle WeatherStatistics mit NaN-Werten (z.B. bei fehlenden Daten)
    WeatherStatistics stats = new WeatherStatistics(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0);

    // Assert: Überprüfe dass NaN-Werte korrekt gespeichert wurden
    assertTrue(Double.isNaN(stats.min()));
    assertTrue(Double.isNaN(stats.max()));
    assertTrue(Double.isNaN(stats.mean()));
    assertTrue(Double.isNaN(stats.median()));
    assertEquals(0.0, stats.coldPercentage());
    assertEquals(0.0, stats.hotPercentage());
  }

  /**
   * Test: Prozentwerte liegen im gültigen Bereich (0-100%)
   * Erwartet: Cold- und Hot-Prozentwerte sind zwischen 0.0 und 100.0
   */
  @Test
  @DisplayName("Prozentagen sollten zwischen 0 und 100 liegen")
  void testPercentageRanges() {
    // Assert: Überprüfe dass beide Prozentwerte im gültigen Bereich liegen
    assertTrue(stats.coldPercentage() >= 0.0 && stats.coldPercentage() <= 100.0);
    assertTrue(stats.hotPercentage() >= 0.0 && stats.hotPercentage() <= 100.0);
  }

  /**
   * Test: Min-Wert ist kleiner oder gleich Max-Wert
   * Erwartet: Mathematische Korrektheit - Minimum kann nicht größer als Maximum sein
   */
  @Test
  @DisplayName("Min sollte kleiner oder gleich Max sein")
  void testMinLessOrEqualMax() {
    // Assert: Überprüfe mathematische Korrektheit der Min/Max-Beziehung
    assertTrue(stats.min() <= stats.max());
  }
}
