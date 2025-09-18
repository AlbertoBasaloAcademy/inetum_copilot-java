public class Weather {
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
    java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
    java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
        .uri(java.net.URI.create(url))
        .header("Accept", "application/json")
        .GET()
        .build();
    java.net.http.HttpResponse.BodyHandler<String> bodyHandlers = java.net.http.HttpResponse.BodyHandlers.ofString();
    try {
      java.net.http.HttpResponse<String> response = client.send(request, bodyHandlers);
      int status = response.statusCode();
      String body = response.body();
      if (status >= 200 && status < 300) {
        java.util.Map<String, Object> m = new JsonParser(body).parseObject();
        Object currentObj = m.get("current");
        Object unitsObj = m.get("current_units");
        double temperature = 0.0;
        String tempUnit = "°C";
        if (currentObj instanceof java.util.Map) {
          java.util.Map<?, ?> current = (java.util.Map<?, ?>) currentObj;
          Object tempVal = current.get("temperature");
          if (tempVal instanceof Number) {
            temperature = ((Number) tempVal).doubleValue();
          }
        }
        if (unitsObj instanceof java.util.Map) {
          Object unit = ((java.util.Map<?, ?>) unitsObj).get("temperature");
          if (unit instanceof String) {
            tempUnit = (String) unit;
          }
        }
        // Precipitation probability and weather condition (code)
        double precipitation = 0.0;
        String condition = "Unknown";
        // Try to get hourly precipitation_probability and weathercode for current hour
        Object hourlyObj = m.get("hourly");
        if (hourlyObj instanceof java.util.Map) {
          java.util.Map<?, ?> hourly = (java.util.Map<?, ?>) hourlyObj;
          Object timeArr = hourly.get("time");
          Object precipArr = hourly.get("precipitation_probability");
          Object codeArr = hourly.get("weathercode");
          if (timeArr instanceof java.util.List && precipArr instanceof java.util.List
              && codeArr instanceof java.util.List) {
            java.util.List<?> times = (java.util.List<?>) timeArr;
            java.util.List<?> precips = (java.util.List<?>) precipArr;
            java.util.List<?> codes = (java.util.List<?>) codeArr;
            String now = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC).toString().substring(0, 13); // e.g.
                                                                                                            // 2025-09-18T14
            int idx = -1;
            for (int i = 0; i < times.size(); i++) {
              Object t = times.get(i);
              if (t instanceof String && ((String) t).startsWith(now)) {
                idx = i;
                break;
              }
            }
            if (idx >= 0 && idx < precips.size() && idx < codes.size()) {
              Object p = precips.get(idx);
              Object c = codes.get(idx);
              if (p instanceof Number) {
                precipitation = ((Number) p).doubleValue();
              }
              if (c instanceof Number) {
                condition = mapWeatherCode(((Number) c).intValue());
              }
            }
          }
        }
        String cityName = city != null && !city.isBlank() ? city : "your city";
        System.out.println("\uD83C\uDF24\uFE0F Weather in " + cityName + ":");
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
