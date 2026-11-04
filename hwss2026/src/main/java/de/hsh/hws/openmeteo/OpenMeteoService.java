package de.hsh.hws.openmeteo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Simple service class to request historical weather data using the REST API
 * from Open Meteo. This class should remain unchanged for the project work.
 */
public class OpenMeteoService {

  /**
   * The base URL to the Open meteo weather archive.
   */
  private static final String URL_OPEN_METEO = "https://archive-api.open-meteo.com/v1/archive";

  /**
   * The latitude for Campus Linden @ Hannover.
   */
  private static final double LATITUDE_HSH = 52.3531;

  /**
   * The longitude for Campus Linden @ Hannover.
   */
  private static final double LONGITUDE_HSH = 9.7243;

  /**
   * The time zone for Europe/Berlin as used in Open meteo API requests.
   */
  private static final String TIME_ZONE = "Europe/Berlin";

  /**
   * The parameter to request temperature data in Open meteo API requests.
   */
  private static final String PARAMETER_TEMPERATURE = "temperature_2m";

  /**
   * This method retrieves the hourly temperatures in Celsius for the specified
   * period and returns the values as a double array (24 entries for each day). If
   * no data is available for a given hour, the corresponding array value is set
   * to NaN. This situation can occur, for example, when the selected period
   * includes the current day and some future hours do not yet have data. In case
   * of any error (connection error, server rejects the request), the method
   * throws an OpenMeteoException exception. Therefore, it should be checked
   * beforehand if the dates of the period are valid.
   * 
   * @param startDate: the start date of the period
   * @param endDate:   the end date of the period
   * @return the values in Celsius as a double array
   */

  //Öffentliche Methode getTemperature
  public double[] getTemperature(LocalDate startDate, LocalDate endDate) {
    return getData(startDate, endDate, PARAMETER_TEMPERATURE);
  }

  /**
   * Internal method to retrieve the hourly data for the specified period from the
   * open Open-Meteo REST API. The JSON response is parsed and the data is
   * converted into a simple double array. Null values are replaced by NaN and any
   * kind of errors are translated to an OpenMeteoException.
   * 
   * @param startDate:     the start date of the period
   * @param endDate:       the end date of the period
   * @param typeParameter: the type parameter used for the request
   * @return the values as a double array
   */

   //Private Methode getData – URL-Aufbau
  private double[] getData(LocalDate startDate, LocalDate endDate, String typeParameter) {
    if (startDate == null || endDate == null) {
      throw new OpenMeteoServiceException("Start date and end date must not be null");
    }
    if (startDate.isAfter(endDate)) {
      throw new OpenMeteoServiceException("Start date must be before or equal to end date");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new OpenMeteoServiceException("End date must not be in the future");
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
      String apiUrl = URL_OPEN_METEO 
      + "?latitude=" + LATITUDE_HSH 
      + "&longitude=" + LONGITUDE_HSH 
      + "&start_date=" + startDate.format(formatter) 
      + "&end_date=" + endDate.format(formatter) 
      + "&hourly=" + typeParameter
      + "&timezone=" + TIME_ZONE;

    //HTTP-Anfrage senden & Antwort prüfen      
      //Aufbau API REQUEST
        HttpClient client = HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(10))
          .build(); //erstellt einen HTTP-Client mit Verbindungs-Timeout
        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .timeout(Duration.ofSeconds(30))
          .build(); //baut die Anfrage mit Builder-Muster

      //Aufbau Request und Aufbau Response
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //endet die Anfrage und empfängt die Antwort als String (blockierende Methode)

      //HTTP-Statuscode
      if (response.statusCode() != 200) {
        throw new IllegalArgumentException("Bad HTTP-Status Code " + response.statusCode());
      }

      //JSON parsen & in Double-Array umwandeln
      JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();//wandelt den Text in Java-Objekt um

      JsonArray raw = json.getAsJsonObject("hourly").getAsJsonArray(typeParameter);//"hourly"-Abschnitt des JSONs & holt das Array

      double[] data = new double[raw.size()];//erstellt ein leeres Zahlen-Array mit der gleichen Länge wie das JSON-Array
/*
for-Schleife geht jeden Eintrag durch:
    - Wenn der Wert nicht null ist → speichere die Zahl als double
    - Wenn der Wert null ist (fehlende Daten) → speichere Double.NaN 
*/
      for (int i = 0; i < raw.size(); i++) {
        JsonElement jsonElement = raw.get(i);
        if (!jsonElement.isJsonNull()) {
          data[i] = jsonElement.getAsDouble();
        } else {
          data[i] = Double.NaN;
        }
      }
      return data;

/*
Der catch-Block fängt alle möglichen Fehler auf (Netzwerkfehler, JSON-Fehler, ungültige Daten, ...)
Anstatt den Fehler direkt weiterzugeben, wird er in eine eigene OpenMeteoServiceException verpackt (selbst definierte Ausnahmeklasse)
Das Originalfehler-Objekt e wird als Parameter übergeben, damit der ursprüngliche Fehler nicht verloren geht
 */
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new OpenMeteoServiceException("Open-Meteo request was interrupted.", e);
    } catch (HttpTimeoutException e) {
      throw new OpenMeteoServiceException("Open-Meteo request timed out after 30 seconds.", e);
    } catch (Exception e) {
      String detail = e.getMessage();
      String message = "Open-Meteo request failed" + (detail == null || detail.isBlank() ? "" : ": " + detail);
      throw new OpenMeteoServiceException(message, e);
    }
  }}
