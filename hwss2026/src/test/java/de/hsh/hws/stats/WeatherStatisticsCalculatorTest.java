package de.hsh.hws.stats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test-Klasse für WeatherStatisticsCalculator.
 * Testet die Berechnung aller statistischen Werte aus Temperaturdaten.
 */
@DisplayName("WeatherStatisticsCalculator Tests")
class WeatherStatisticsCalculatorTest {

  private WeatherStatisticsCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new WeatherStatisticsCalculator();
  }

  @Test
  @DisplayName("Calculator mit gültigen Daten sollte alle Werte berechnen")
  void testCalculateWithValidData() {
    // Arrange
    double[] values = {10.0, 20.0, 30.0, 40.0, 50.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertNotNull(stats);
    assertEquals(10.0, stats.min());
    assertEquals(50.0, stats.max());
    assertEquals(30.0, stats.mean());
    assertEquals(30.0, stats.median()); // [10, 20, 30, 40, 50] -> median = 30
  }

  @Test
  @DisplayName("Minimum sollte korrekt berechnet werden")
  void testCalculateMin() {
    // Arrange
    double[] values = {15.5, 10.2, 25.8, 5.1, 30.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(5.1, stats.min());
  }

  @Test
  @DisplayName("Maximum sollte korrekt berechnet werden")
  void testCalculateMax() {
    // Arrange
    double[] values = {15.5, 10.2, 25.8, 5.1, 30.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(30.0, stats.max());
  }

  @Test
  @DisplayName("Mittelwert sollte korrekt berechnet werden")
  void testCalculateMean() {
    // Arrange
    double[] values = {10.0, 20.0, 30.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(20.0, stats.mean());
  }

  @Test
  @DisplayName("Median mit ungerader Anzahl sollte korrekt berechnet werden")
  void testCalculateMedianOddCount() {
    // Arrange
    double[] values = {1.0, 2.0, 3.0, 4.0, 5.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(3.0, stats.median());
  }

  @Test
  @DisplayName("Median mit gerader Anzahl sollte korrekt berechnet werden")
  void testCalculateMedianEvenCount() {
    // Arrange
    double[] values = {1.0, 2.0, 3.0, 4.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(2.5, stats.median()); // (2.0 + 3.0) / 2.0 = 2.5
  }

  @Test
  @DisplayName("Cold-Prozentage sollte korrekt berechnet werden")
  void testCalculateColdPercentage() {
    // Arrange
    double[] values = {-5.0, 0.0, 10.0, 20.0}; // 1 Wert < 0.0

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(25.0, stats.coldPercentage()); // 1/4 * 100 = 25%
  }

  @Test
  @DisplayName("Hot-Prozentage sollte korrekt berechnet werden")
  void testCalculateHotPercentage() {
    // Arrange
    double[] values = {10.0, 25.0, 35.0, 40.0}; // 2 Werte > 30.0

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(50.0, stats.hotPercentage()); // 2/4 * 100 = 50%
  }

  @Test
  @DisplayName("Calculator mit NaN-Werten sollte diese ignorieren")
  void testCalculateWithNaN() {
    // Arrange
    double[] values = {10.0, Double.NaN, 20.0, Double.NaN, 30.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertNotNull(stats);
    assertEquals(10.0, stats.min());
    assertEquals(30.0, stats.max());
    assertEquals(20.0, stats.mean()); // (10+20+30)/3 = 20
  }

  @Test
  @DisplayName("Calculator mit nur NaN-Werten sollte NaN zurückgeben")
  void testCalculateWithOnlyNaN() {
    // Arrange
    double[] values = {Double.NaN, Double.NaN, Double.NaN};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertTrue(Double.isNaN(stats.min()));
    assertTrue(Double.isNaN(stats.max()));
    assertTrue(Double.isNaN(stats.mean()));
    assertTrue(Double.isNaN(stats.median()));
    assertEquals(0.0, stats.coldPercentage());
    assertEquals(0.0, stats.hotPercentage());
  }

  @Test
  @DisplayName("Calculator mit null-Array sollte NaN zurückgeben")
  void testCalculateWithNull() {
    // Act
    WeatherStatistics stats = calculator.calculate(null);

    // Assert
    assertTrue(Double.isNaN(stats.min()));
    assertTrue(Double.isNaN(stats.max()));
    assertTrue(Double.isNaN(stats.mean()));
    assertTrue(Double.isNaN(stats.median()));
    assertEquals(0.0, stats.coldPercentage());
    assertEquals(0.0, stats.hotPercentage());
  }

  @Test
  @DisplayName("Calculator mit leerem Array sollte NaN zurückgeben")
  void testCalculateWithEmptyArray() {
    // Arrange
    double[] values = {};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertTrue(Double.isNaN(stats.min()));
    assertTrue(Double.isNaN(stats.max()));
    assertTrue(Double.isNaN(stats.mean()));
    assertTrue(Double.isNaN(stats.median()));
    assertEquals(0.0, stats.coldPercentage());
    assertEquals(0.0, stats.hotPercentage());
  }

  @Test
  @DisplayName("Calculator mit einzelnem Wert sollte korrekt rechnen")
  void testCalculateWithSingleValue() {
    // Arrange
    double[] values = {15.5};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(15.5, stats.min());
    assertEquals(15.5, stats.max());
    assertEquals(15.5, stats.mean());
    assertEquals(15.5, stats.median());
  }

  @Test
  @DisplayName("Calculator mit 24 Stunden sollte korrekt rechnen")
  void testCalculateWith24Hours() {
    // Arrange - Typischer Tagestemperaturverlauf
    double[] values = {
        5.0, 4.5, 4.2, 4.0, 3.8, 3.5, 4.0, 5.5, 8.0, 12.0, 16.0, 18.0,
        19.0, 18.5, 17.0, 15.0, 12.5, 10.0, 8.5, 7.0, 6.0, 5.5, 5.0, 4.5
    };

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertNotNull(stats);
    assertTrue(stats.min() >= 3.0);
    assertTrue(stats.max() <= 20.0);
    assertTrue(stats.mean() > 0.0);
    assertTrue(stats.coldPercentage() >= 0.0 && stats.coldPercentage() <= 100.0);
    assertTrue(stats.hotPercentage() >= 0.0 && stats.hotPercentage() <= 100.0);
  }

  @Test
  @DisplayName("Negative und positive Werte sollten korrekt verarbeitet werden")
  void testCalculateWithNegativeAndPositive() {
    // Arrange
    double[] values = {-10.0, -5.0, 0.0, 5.0, 10.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(-10.0, stats.min());
    assertEquals(10.0, stats.max());
    assertEquals(0.0, stats.mean());
    assertEquals(40.0, stats.coldPercentage()); // 2 Werte < 0.0
    assertEquals(0.0, stats.hotPercentage()); // 0 Werte > 30.0
  }

  @Test
  @DisplayName("Cold-Prozentage sollte nur Werte < 0 zählen")
  void testColdPercentageThreshold() {
    // Arrange
    double[] values = {-1.0, -0.1, 0.0, 0.1, 1.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(40.0, stats.coldPercentage()); // 2/5 = 40%
  }

  @Test
  @DisplayName("Hot-Prozentage sollte nur Werte > 30 zählen")
  void testHotPercentageThreshold() {
    // Arrange
    double[] values = {29.9, 30.0, 30.1, 35.0, 40.0};

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertEquals(60.0, stats.hotPercentage()); // 3/5 = 60% (30.1, 35.0, 40.0 > 30.0)
  }

  @Test
  @DisplayName("Calculator sollte bei großen Datenmengen korrekt rechnen")
  void testCalculateWithLargeDataset() {
    // Arrange
    double[] values = new double[1000];
    for (int i = 0; i < 1000; i++) {
      values[i] = Math.sin(i * 0.01) * 20 + 15; // Sinuswelle mit Offset
    }

    // Act
    WeatherStatistics stats = calculator.calculate(values);

    // Assert
    assertNotNull(stats);
    assertFalse(Double.isNaN(stats.min()));
    assertFalse(Double.isNaN(stats.max()));
    assertFalse(Double.isNaN(stats.mean()));
    assertFalse(Double.isNaN(stats.median()));
  }
}
