Un comando para obtener el clima actual de una ciudad específica utilizando una API de clima pública.
EL api es https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.419998&current_weather=true

el resultado es un json con la temperatura actual y otros datos meteorológicos.
```json
{
  "latitude": 52.52,
  "longitude": 13.419998,
  "generationtime_ms": 0.009059906005859375,
  "utc_offset_seconds": 7200,
  "timezone": "Europe/Berlin",
  "timezone_abbreviation": "GMT+2",
  "elevation": 38,
  "current_weather": {
    "temperature": 18.3,
    "windspeed": 11.3,
    "winddirection": 230,
    "weathercode": 3,
    "is_day": 1,
    "time": "2024-09-13T08:00"
  }
}
```
espero que lo muestres así:

🌤️ Weather in Madrid:
   Temperature: 22°C
   Precipitation: 15%
   Condition: Partly cloudy
