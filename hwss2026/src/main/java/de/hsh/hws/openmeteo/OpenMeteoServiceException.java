package de.hsh.hws.openmeteo;

/**
 * Simple exception class used by the OpenMeteoService class in case of
 * connection problems or any other issues.
 */
public class OpenMeteoServiceException extends RuntimeException {

  public OpenMeteoServiceException(String message) {
    super(message);
  }

  public OpenMeteoServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
