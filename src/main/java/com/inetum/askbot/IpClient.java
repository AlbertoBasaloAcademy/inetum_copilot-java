package com.inetum.askbot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

/**
 * Utility class for fetching and displaying IP address information.
 * Uses the ipapi.co service to retrieve geolocation data based on the user's
 * IP.
 */
public class IpClient {
  private IpClient() {
  }

  /**
   * Prints the IP address and location information to the console.
   *
   * @param ipApi the IP API response containing location data
   */
  public static void printIp(IpApiResponse ipApi) {
    System.out.println("## Your IP address is " + ipApi.ip());
    String location = ipApi.city() + ", " + ipApi.region() + ", " + ipApi.country_name();
    System.out.println("- Location: " + location);
    String coordinates = "- Coordinates: Lat " + ipApi.latitude() + ", Long " + ipApi.longitude();
    System.out.println(coordinates);
  }

  /**
   * Fetches IP address information from the ipapi.co API.
   * Makes an HTTP GET request and parses the JSON response.
   *
   * @return the parsed IP API response, or null if the request fails
   */
  public static IpApiResponse fetchIp() {
    HttpClient client = HttpClient.newHttpClient();

    BodyHandler<String> bodyHandlers = HttpResponse.BodyHandlers.ofString();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://ipapi.co/json/"))
        .header("Accept", "application/json")
        .GET()
        .build();

    try {
      HttpResponse<String> response = client.send(request, bodyHandlers);
      int status = response.statusCode();
      String body = response.body();

      if (status >= 200 && status < 300) {
        return IpApiResponse.fromJson(body);
      }
      System.out.println("Error HTTP " + status);
    } catch (IOException e) {
      System.out.println("Could not fetch IP address.");
      e.printStackTrace();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    return null;
  }
}
