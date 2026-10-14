package de.hsh.hws.openmeteo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

/**
 * Test-Klasse für OpenMeteoService mit Mocks für HTTP-Requests.
 * Da die OpenMeteoService nur interne HTTP-Requests verwendet,
 * testen wir die Fehlerbehandlung durch echte HTTP-Aufrufe.
 */
@DisplayName("OpenMeteoService Tests")
class OpenMeteoServiceTest {

  private OpenMeteoService service;

  @BeforeEach
  void setUp() {
    service = new OpenMeteoService();
  }

  @Test
  @DisplayName("getTemperature mit gültigen Daten sollte Double-Array zurückgeben")
  void testGetTemperatureWithValidData() {
    // Arrange
    LocalDate startDate = LocalDate.of(2024, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 1, 2);

    // Act & Assert - Echter API-Aufruf
    double[] result = service.getTemperature(startDate, endDate);

    // Verifizieren
    assertNotNull(result);
    assertEquals(48, result.length); // 2 Tage × 24 Stunden
    // Überprüfe, dass Werte entweder Zahlen oder NaN sind
    for (double value : result) {
      assertTrue(Double.isFinite(value) || Double.isNaN(value));
    }
  }

  @Test
  @DisplayName("getTemperature mit ungültigen Daten sollte Exception werfen")
  void testGetTemperatureWithInvalidDates() {
    // Arrange
    LocalDate startDate = LocalDate.of(2024, 1, 2);
    LocalDate endDate = LocalDate.of(2024, 1, 1); // endDate vor startDate

    // Act & Assert
    assertThrows(OpenMeteoServiceException.class, () -> {
      service.getTemperature(startDate, endDate);
    });
  }

  @Test
  @DisplayName("getTemperature mit zukünftigen Daten sollte Exception werfen")
  void testGetTemperatureWithFutureData() {
    // Arrange
    LocalDate mockedToday = LocalDate.of(2026, 5, 13);
    LocalDate tomorrow;
    try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class)) {
      mockedLocalDate.when(LocalDate::now).thenReturn(mockedToday);
      tomorrow = LocalDate.now().plusDays(1);

      // Act & Assert - Die Open-Meteo API liefert keine Vorhersagen, daher die Validierung schlägt fehl
      assertThrows(OpenMeteoServiceException.class, () -> {
        service.getTemperature(tomorrow, tomorrow);
      });
    }
  }

  @Test
  @DisplayName("getTemperature mit historischen Daten sollte Array zurückgeben")
  void testGetTemperatureWithHistoricalData() {
    // Arrange
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2020, 1, 5);

    // Act & Assert
    double[] result = service.getTemperature(startDate, endDate);

    // Verifizieren
    assertNotNull(result);
    assertEquals(120, result.length); // 5 Tage × 24 Stunden
  }

  @Test
  @DisplayName("getTemperature mit einzelnem Tag sollte 24 Einträge zurückgeben")
  void testGetTemperatureWithSingleDay() {
    // Arrange
    LocalDate startDate = LocalDate.of(2023, 6, 15);
    LocalDate endDate = LocalDate.of(2023, 6, 15);

    // Act & Assert
    double[] result = service.getTemperature(startDate, endDate);

    // Verifizieren
    assertNotNull(result);
    assertEquals(24, result.length);
  }

  @Test
  @DisplayName("getTemperature sollte OpenMeteoServiceException werfen")
  void testGetTemperatureExceptionHandling() {
    // Arrange
    LocalDate startDate = LocalDate.of(2024, 1, 2);
    LocalDate endDate = LocalDate.of(2024, 1, 1); // Ungültiges Datum

    // Act & Assert
    OpenMeteoServiceException exception = assertThrows(OpenMeteoServiceException.class, () -> {
      service.getTemperature(startDate, endDate);
    });

    assertNotNull(exception.getMessage());
  }

  @Test
  @DisplayName("getTemperature mit verschiedenen Zeiträumen sollte funktionieren")
  void testGetTemperatureWithDecemberDate() {
    // Arrange
    LocalDate startDate = LocalDate.of(2023, 12, 25);
    LocalDate endDate = LocalDate.of(2023, 12, 26);

    // Act & Assert
    double[] result = service.getTemperature(startDate, endDate);

    // Verifizieren
    assertNotNull(result);
    assertEquals(48, result.length); // 2 Tage × 24 Stunden
    // Überprüche Wertebereiche für realistische Temperaturen
    for (double temp : result) {
      if (!Double.isNaN(temp)) {
        assertTrue(temp > -50 && temp < 60, "Temperatur sollte realistisch sein");
      }
    }
  }

  @Test
  @DisplayName("getTemperature mit null-Parametern sollte OpenMeteoServiceException werfen")
  void testGetTemperatureWithNullParameters() {
    // Act & Assert - Null-Werte führen zu OpenMeteoServiceException
    assertThrows(OpenMeteoServiceException.class, () -> {
      service.getTemperature(null, LocalDate.of(2024, 1, 1));
    });

    assertThrows(OpenMeteoServiceException.class, () -> {
      service.getTemperature(LocalDate.of(2024, 1, 1), null);
    });
  }
}
