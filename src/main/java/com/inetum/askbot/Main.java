package com.inetum.askbot;

/**
 * Main entry point for the Ask Bot application.
 * This application fetches IP information and optionally weather or currency
 * data based on command line arguments.
 */
public class Main {
  /**
   * Main method that starts the application.
   * Fetches IP information and processes command line arguments to fetch weather
   * or currency data.
   *
   * @param args command line arguments: "weather" or "money"
   */
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

  /**
   * Prints the help message showing available commands.
   */
  private static void printHelpMessage() {
    System.out.println("## Available commands: ");
    System.out.println("  - `weather` : Fetch weather information");
    System.out.println("  - `money` : Fetch currency information");
  }
}
