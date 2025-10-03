package com.inetum.askbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.Map;

public class Money {
  private Money() {
  }

  public static void fetchMoney() {
    IpApiResponse ipApi = IpClient.fetchIp();
    if (ipApi == null) {
      System.out.println("Could not determine your location. Please check your internet connection.");
      return;
    }
    String currency = ipApi.currency();
    String currencyName = ipApi.currency_name();
    if (currency == null || currency.isBlank()) {
      System.out.println("Could not determine your country's currency.");
      return;
    }
    String url = "https://api.frankfurter.dev/v1/latest?from=" + currency;

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
        Object ratesObj = parsed.get("rates");
        if (ratesObj instanceof Map<?, ?> rates) {
          System.out.println("💰 Currency: " + currencyName + " (" + currency + ")");
          System.out.println("   1 " + currency + " = 1.00 " + currency);
          printRate(rates, currency, "EUR");
          printRate(rates, currency, "USD");
          printRate(rates, currency, "GBP");
          printRate(rates, currency, "CHF");
        } else {
          System.out.println("Could not parse currency rates.");
        }
      } else {
        System.out.println("Could not fetch currency data (HTTP " + status + "). Try again later.");
      }
    } catch (Exception e) {
      System.out.println("Could not fetch currency data. Please check your connection.");
    }
  }

  private static void printRate(Map<?, ?> rates, String base, String target) {
    Object rateObj = rates.get(target);
    if (rateObj instanceof Number number) {
      double rate = number.doubleValue();
      System.out.println("   1 " + base + " = " + String.format("%.2f", rate) + " " + target);
    }
  }
}