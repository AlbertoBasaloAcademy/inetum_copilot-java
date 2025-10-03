package com.inetum.askbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * Utility class for fetching and displaying weather information.
 * Retrieves current weather data using the Open-Meteo API based on the user's
 * location from IP.
 */
public class Weather {
  private Weather() {
  }

  /**
   * Fetches and displays current weather information for the user's location.
   * First fetches IP location, then queries Open-Meteo API for weather data
   * including temperature,
   * precipitation probability, and weather condition.
   */
  public static void fetchWeather() {
    IpApiResponse ipApi = IpClient.fetchIp();
    if (ipApi == null) {
      System.out.println("Could not determine your location. Please check your internet connection.");
      return;
    }
    double lat = ipApi.latitude();
    double lon = ipApi.longitude();
    String city = ipApi.city();
    String url = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
        + "&current_weather=true&hourly=precipitation_probability,weathercode";

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Accept", "application/json")
        .GET()
        .build();
    BodyHandler<String> bodyHandlers = HttpResponse.BodyHandlers.ofString();
    try {
      HttpResponse<String> response = client.send(request, bodyHandlers);
      int status = response.statusCode();
      String body = response.body();
      if (status >= 200 && status < 300) {
        Map<String, Object> parsed = new JsonParser(body).parseObject();
        Object currentObj = parsed.get("current");
        Object unitsObj = parsed.get("current_units");
        double temperature = extractTemperature(currentObj);
        String tempUnit = extractTemperatureUnit(unitsObj);
        double precipitation = 0.0;
        String condition = "Unknown";

        Object hourlyObj = parsed.get("hourly");
        if (hourlyObj instanceof Map<?, ?> hourly) {
          Object timeArr = hourly.get("time");
          Object precipArr = hourly.get("precipitation_probability");
          Object codeArr = hourly.get("weathercode");
          var currentIndex = findCurrentHourIndex(timeArr);
          if (currentIndex >= 0) {
            precipitation = extractAtIndex(precipArr, currentIndex);
            condition = mapCodeToCondition(codeArr, currentIndex);
          }
        }
        String cityName = (city != null && !city.isBlank()) ? city : "your city";
        System.out.println("🌤️ Weather in " + cityName + ":");
        System.out.println("\t Temperature: " + Math.round(temperature) + tempUnit);
        System.out.println("\t Precipitation: " + Math.round(precipitation) + "%");
        System.out.println("\t Condition: " + condition);
      } else {
        System.out.println("Could not fetch weather data (HTTP " + status + "). Try again later.");
      }
    } catch (Exception e) {
      System.out.println("Could not fetch weather data. Please check your connection.");
    }
  }

  /**
   * Extracts the temperature value from the current weather object.
   *
   * @param currentObj the current weather map from the API response
   * @return the temperature as a double, or 0.0 if not found
   */
  private static double extractTemperature(Object currentObj) {
    if (currentObj instanceof Map<?, ?> current) {
      Object tempVal = current.get("temperature");
      if (tempVal instanceof Number number) {
        return number.doubleValue();
      }
    }
    return 0.0;
  }

  /**
   * Extracts the temperature unit from the current units object.
   *
   * @param unitsObj the current units map from the API response
   * @return the temperature unit string, or "°C" if not found
   */
  private static String extractTemperatureUnit(Object unitsObj) {
    if (unitsObj instanceof Map<?, ?> units) {
      Object unit = units.get("temperature");
      if (unit instanceof String str) {
        return str;
      }
    }
    return "°C";
  }

  /**
   * Finds the index of the current hour in the hourly time array.
   * Matches the current UTC time (truncated to hour) with the time strings in the
   * array.
   *
   * @param timeArr the list of time strings from the API
   * @return the index of the current hour, or -1 if not found
   */
  private static int findCurrentHourIndex(Object timeArr) {
    if (timeArr instanceof List<?> times) {
      String now = LocalDateTime.now(ZoneOffset.UTC).toString().substring(0, 13);
      for (int i = 0; i < times.size(); i++) {
        Object value = times.get(i);
        if (value instanceof String str && str.startsWith(now)) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Extracts a numeric value at the specified index from an array.
   *
   * @param array the list to extract from
   * @param index the index to extract at
   * @return the value as a double, or 0.0 if not found or invalid
   */
  private static double extractAtIndex(Object array, int index) {
    if (array instanceof List<?> list && index >= 0 && index < list.size()) {
      Object value = list.get(index);
      if (value instanceof Number number) {
        return number.doubleValue();
      }
    }
    return 0.0;
  }

  /**
   * Maps a weather code at the specified index to a human-readable condition
   * string.
   *
   * @param array the list of weather codes
   * @param index the index to map
   * @return the condition string, or "Unknown" if not found
   */
  private static String mapCodeToCondition(Object array, int index) {
    if (array instanceof List<?> list && index >= 0 && index < list.size()) {
      Object value = list.get(index);
      if (value instanceof Number number) {
        return mapWeatherCode(number.intValue());
      }
    }
    return "Unknown";
  }

  /**
   * Maps a numeric weather code to a descriptive weather condition string.
   * Uses the WMO weather interpretation codes.
   *
   * @param code the weather code from the API
   * @return the corresponding weather condition description
   */
  private static String mapWeatherCode(int code) {
    return switch (code) {
      case 0 -> "Clear sky";
      case 1, 2 -> "Partly cloudy";
      case 3 -> "Overcast";
      case 45, 48 -> "Foggy";
      case 51, 53, 55 -> "Drizzle";
      case 56, 57 -> "Freezing Drizzle";
      case 61, 63, 65 -> "Rain";
      case 66, 67 -> "Freezing Rain";
      case 71, 73, 75, 77 -> "Snow";
      case 80, 81, 82 -> "Rain showers";
      case 85, 86 -> "Snow showers";
      case 95 -> "Thunderstorm";
      case 96, 99 -> "Thunderstorm with hail";
      default -> "Unknown";
    };
  }
}
