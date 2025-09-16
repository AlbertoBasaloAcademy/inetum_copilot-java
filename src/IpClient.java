import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

public class IpClient {
  public static void printIp(IpApiResponse ipApi) {
    System.out.println("## Your IP address is " + ipApi.ip());
    String location = ipApi.city() + ", " + ipApi.region() + ", " + ipApi.country_name();
    System.out.println("- Location: " + location);
    String coordinates = "- Coordinates: Lat " + ipApi.latitude() + ", Long " + ipApi.longitude();
    System.out.println(coordinates);
  }

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
        IpApiResponse ipApi = IpApiResponse.fromJson(body);
        return ipApi;
      } else {
        System.out.println("Error HTTP " + status);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    return null;
  }
}
