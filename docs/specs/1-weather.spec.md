## 1. Problem Specification (from the user POV)
As a user, I want to know today's weather forecast for my current city, including temperature, chance of rain, and weather conditions, so I can plan my day accordingly.

## 2. Solution Overview (from the dev POV)

### Remote URL
- Geolocation: `http://ip-api.com/json/`
- Weather: `https://api.open-meteo.com/v1/forecast?latitude=<lat>&longitude=<lon>&current_weather=true`

### Expected API Response
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

### Report to User (Example Output)
```
🌤️ Weather in Madrid:
	 Temperature: 22°C
	 Precipitation: 15%
	 Condition: Partly cloudy
```

## 3. Acceptance Criteria (from the QA)
- The `weather` command returns the current temperature, precipitation probability, and weather condition for the user's city.
- Output is structured, readable, and matches the format in the PRD examples.
- If the weather API or geolocation API is unavailable, the CLI shows a clear, friendly error message.
- The command responds within 3 seconds under normal network conditions.
- The CLI can be run from the terminal without errors or crashes.
