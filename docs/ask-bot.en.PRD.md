---
ID: 'DEMO_ASK-BOT'
Curso: 'Copilot-Java'
Arquetipo: 'Java-CLI'
Autor: 'Alberto Basalo'
---
# PRD — AskBot CLI

## 1. Executive Summary

An educational CLI that queries public APIs to provide basic information about the user's IP and associated services: location, weather, currency, time, and sun data. The goal is to present students with a working terminal program that demonstrates consuming external services and handling JSON data.

### 1.1 Goals and Metrics

- Build a functional CLI application with all commands operating locally.
- Demonstrate successful consumption of public, free REST APIs.
- Implement error handling for external services that may be unavailable.

### 1.2 Audience

- Students: to gain hands-on experience consuming APIs.
- Instructors: to use as a guide and assess learning.

### 1.3 Technologies
- Language: Java 21
- Libraries: Minimal, preferably no extra frameworks.
- Tools: Visual Studio Code or IntelliJ IDEA.
- Deployment: Executable via `java -jar <jar-name>.jar`.

## 2. Scope

- Included: CLI with 4 main commands to query geolocation, weather, currency, and solar data.
- Excluded: Graphical interface, data persistence, authentication, notifications.

### 2.1 User Stories

- As a user, I want to know my current location based on my IP.
- As a user, I want to know today's forecast for my city.
- As a user, I want to see my country's official currency and basic exchange rates.
- As a user, I want to know sunrise and sunset times.

### 2.2 Functional Requirements

1. RF-1: `loc` or [empty] — Shows city, country, latitude/longitude obtained from ipapi.co or ip-api.com.
2. RF-2: `weather` — Shows current temperature, chance of rain, and weather code from open-meteo.com.
3. RF-3: `money` — Shows the official currency of the country and exchange rates to EUR/USD/GBP/CHF from frankfurter.dev.
4. RF-4: `sun` — Shows sunrise and sunset times from open-meteo.com.

### 2.3 Additional Functional Requirements

5. RF-5: `askbot help` — Displays help with a list of available commands and descriptions.
6. RF-6: `askbot --version` — Shows the application's current version.
7. RF-7: All commands must handle connectivity errors and show informative messages.

### 2.4 Non-Functional Requirements

- Simplicity: Prioritize clarity and simplicity in implementation and usage.
- Performance: CLI responses should be fast, preferably under 1 second with an active connection.
- Availability: Gracefully handle unavailability of external services.
- Usability: Intuitive commands and clear, useful error messages.


## 3. Data Model and APIs

### 3.1 Geolocation

- `curl http://ip-api.com/json/`

```json
{
  "status": "success",
  "country": "Spain",
  "countryCode": "ES",
  "region": "GA",
  "regionName": "Galicia",
  "city": "A Coruña",
  "zip": "15009",
  "lat": 43.3626,
  "lon": -8.4012,
  "timezone": "Europe/Madrid",
  "isp": "Telefonica de Espana SAU",
  "org": "RIMA (Red IP Multi Acceso)",
  "as": "AS3352 TELEFONICA DE ESPANA S.A.U.",
  "query": "81.39.197.0"
}
```

### 3.2 Weather

- `curl "https://api.open-meteo.com/v1/forecast?latitude=43.3626&longitude=-8.4012&current_weather=true"`
```json
{
  "latitude": 43.3626,
  "longitude": -8.4012,
  "generationtime_ms": 0.02491474151611328,
  "utc_offset_seconds": 0,
  "timezone": "GMT",
  "timezone_abbreviation": "GMT",
  "elevation": 0,
  "current_units": {
    "time": "iso8601",
    "interval": "seconds",
    "temperature": "°C"
  },
  "current": {
    "time": "2025-09-13T06:45",
    "interval": 900,
    "temperature": 14.3
  }
}
```

### 3.3 Currency

- `curl https://api.frankfurter.dev/v1/latest` — Currency exchange rates

```json
{
  "amount": 1.0,
  "base": "EUR",
  "date": "2024-09-13",
  "rates": {
    "USD": 1.08,
    "GBP": 0.86,
    "CHF": 0.97
  }
}
```

### 3.4 Sun

- `curl "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.419998&daily=sunrise,sunset&timezone=Europe%2FBerlin"` — Sunrise and sunset info

```json
{
  "latitude": 52.52,
  "longitude": 13.419998,
  "generationtime_ms": 0.006198883056640625,
  "utc_offset_seconds": 7200,
  "timezone": "Europe/Berlin",
  "timezone_abbreviation": "GMT+2",
  "elevation": 38,
  "daily_units": {
    "time": "iso8601",
    "sunrise": "iso8601",
    "sunset": "iso8601"
  },
  "daily": {
    "time": [
      "2025-09-13"
    ],
    "sunrise": [
      "2025-09-13T06:37"
    ],
    "sunset": [
      "2025-09-13T19:26"
    ]
  }
}
```

## 4. Acceptance Criteria and Risks

### 4.1 Acceptance Criteria

- Each command returns readable, structured text data.
- The CLI can be run from the terminal without errors.
- Connectivity and API unavailability are handled appropriately.
- Commands respond within 3 seconds under normal conditions.
- The application shows clear help messages when executed without parameters.

### 4.2 Risks

- External APIs not available: implement friendly fallback messages.
- API usage limits: inform the user about possible restrictions.
- Network connectivity: handle timeouts and connection errors gracefully.
- API changes: public APIs may change structure without notice.

## 5. Output Examples

### 5.1 `loc` command

📍 Location:
   City: Madrid
   Country: Spain (ES)
   Coordinates: 40.4168, -3.7038

### 5.2 `weather` command

🌤️ Weather in Madrid:
   Temperature: 22°C
   Precipitation: 15%
   Condition: Partly cloudy

### 5.3 `money` command

💰 Currency: Euro (EUR)
   1 EUR = 1.00 EUR
   1 EUR = 1.08 USD
   1 EUR = 0.86 GBP
   1 EUR = 0.97 CHF

### 5.4 `time` command

🕐 Local Time:
   Current: 14:30:25
   Zone: Europe/Madrid (CET)
   UTC+2 (Daylight Saving Time)

### 5.5 `sun` command

☀️ Solar Info:
   Sunrise: 07:45
   Sunset: 19:20
   Day length: 11h 35m


