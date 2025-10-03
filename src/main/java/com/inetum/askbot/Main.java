package com.inetum.askbot;

public class Main {
  public static void main(String[] args) {
    System.out.println("# Ask Bot here, welcome!");
    IpClient.fetchIp();
    if (args.length > 0) {
      if ("weather".equals(args[0])) {
        System.out.println("## Fetching weather information...");
        Weather.fetchWeather();
      } else if ("money".equals(args[0])) {
        System.out.println("## Fetching currency information...");
        Money.fetchMoney();
      } else {
        printHelpMessage();
      }
    } else {
      printHelpMessage();
    }
    System.out.println("Bye!");
  }

  private static void printHelpMessage() {
    System.out.println("## Available commands: ");
    System.out.println("  - `weather` : Fetch weather information");
    System.out.println("  - `money` : Fetch currency information");
  }
}
