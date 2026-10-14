package de.hsh.hws.app;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import de.hsh.hws.app.WeatherCliController;
import de.hsh.hws.openmeteo.OpenMeteoService;
import de.hsh.hws.stats.WeatherStatistics;
import de.hsh.hws.stats.WeatherStatisticsCalculator;
import de.hsh.hws.stats.WeatherStatisticsPrinter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test-Klasse für WeatherCliController.
 *
 * GETESTETE KLASSE: WeatherCliController
 *
 * WARUM: WeatherCliController ist verantwortlich für:
 * - Orchestrierung der Wetterdaten-Verarbeitung (Controller-Pattern)
 * - Koordination zwischen Service (OpenMeteoService), Berechnung (WeatherStatisticsCalculator)
 *   und Ausgabe (WeatherStatisticsPrinter)
 * - Integration der verschiedenen Komponenten für die CLI-Anwendung
 * - Fehlerbehandlung und Datenfluss-Management
 *
 * TESTMETHODEN:
 * - fetchWeatherData(): Holt Temperaturdaten vom Service
 * - computeStatistics(): Berechnet Statistiken aus Temperaturdaten
 *
 * TESTTECHNIKEN:
 * - Mocking: Alle Abhängigkeiten werden gemockt (OpenMeteoService, Calculator, Printer)
 * - Unit-Testing: Testet isolierte Methoden ohne echte API-Aufrufe oder Berechnungen
 * - Integration-Testing: Überprüft korrekte Zusammenarbeit der Komponenten
 */
public class WeatherCliControllerTest {

    @Mock
    private OpenMeteoService mockService;

    @Mock
    private WeatherStatisticsCalculator mockCalculator;

    @Mock
    private WeatherStatisticsPrinter mockPrinter;

    private WeatherCliController controller;
    private PrintStream out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisiert die Mocks
        out = new PrintStream(new ByteArrayOutputStream());
        DateInputManager dummyInput = new DateInputManager(new Scanner(""), out);
        controller = new WeatherCliController(mockService, mockCalculator, mockPrinter, dummyInput, out);
    }

    /**
     * Test: fetchWeatherData holt korrekt Temperaturdaten vom Service
     * Erwartet: Temperatur-Array wird vom Service zurückgegeben und unverändert weitergeleitet
     */
    @Test
    void testFetchWeatherData() {
        // Arrange: Definiert das Verhalten der Mocks - Service gibt Temperatur-Array zurück
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 2);
        double[] expectedTemperatures = {10.0, 12.0, 15.0};
        when(mockService.getTemperature(start, end)).thenReturn(expectedTemperatures);

        // Act: Ruft die protected Methode auf, die den Service verwendet
        double[] result = controller.fetchWeatherData(start, end);

        // Assert: Überprüft das Ergebnis und den Mock-Aufruf - Service wurde korrekt aufgerufen
        assertArrayEquals(expectedTemperatures, result);
        verify(mockService).getTemperature(start, end);
    }

    /**
     * Test: computeStatistics berechnet korrekt Statistiken aus Temperaturdaten
     * Erwartet: WeatherStatistics-Objekt wird vom Calculator zurückgegeben und unverändert weitergeleitet
     */
    @Test
    void testComputeStatistics() {
        // Arrange: Setup mit Temperatur-Array und gemocktem Statistics-Objekt
        double[] temperatures = {10.0, 20.0, 30.0};
        WeatherStatistics mockStats = mock(WeatherStatistics.class);
        when(mockCalculator.calculate(temperatures)).thenReturn(mockStats);

        // Act: Ruft die protected Methode auf, die den Calculator verwendet
        WeatherStatistics result = controller.computeStatistics(temperatures);

        // Assert: Überprüft dass das Statistics-Objekt korrekt zurückgegeben wird und Calculator aufgerufen wurde
        assertEquals(mockStats, result);
        verify(mockCalculator).calculate(temperatures);
    }
}