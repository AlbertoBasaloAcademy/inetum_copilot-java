# Backlog for inetum_copilot-java

> Epic Priority Legend: ‼️ Critical | ❗ High  |❕ Normal

> Feature Status Legend: ⛔ BLOCKED | ⏳ PENDING | ✨ CODED | ✅ TESTED 

## E1 Ask Bot CLI ❗

CLI educational tool that queries public APIs to provide basic information from the user's IP and associated services: location, weather, currency, and sun times.

### F1.1 IP Location ✨

- **Dependencies:** None
- **Project Requirements:** RF-1 from ask-bot.PRD.md - Display city, country, latitude/longitude from IP API.
- Fetches and displays user's geolocation based on IP address.

### F1.2 Weather ✨

- **Dependencies:** F1.1
- **Project Requirements:** RF-2 from ask-bot.PRD.md - Show current temperature, precipitation probability, and weather condition.
- Displays current weather information for the user's location using geolocation data.

### F1.3 Currency ⏳

- **Dependencies:** F1.1
- **Project Requirements:** RF-3 from ask-bot.PRD.md - Display official currency and exchange rates to EUR/USD/GBP/CHF.
- Shows the country's official currency and basic exchange rates.

### F1.4 Sun Times ⏳

- **Dependencies:** F1.1
- **Project Requirements:** RF-4 from ask-bot.PRD.md - Display sunrise and sunset times.
- Provides sunrise and sunset information for the user's location.

> End of BACKLOG for inetum_copilot-java, last updated October 3, 2025.