## 1. Problem Specification (from the user POV)
As a user, I want to see my country's official currency and basic exchange rates to EUR, USD, GBP, and CHF, so I can understand the current value and conversion rates for my local currency.

## 2. Solution Overview (from the dev POV)

### Remote URLs
- Geolocation: `http://ip-api.com/json/` (to get country code)
- Currency Info: Use a static mapping or additional API to get the official currency code for the country
- Exchange Rates: `https://api.frankfurter.dev/v1/latest?from=<currency_code>` (where <currency_code> is the country's official currency)

### Expected API Response (for EUR as example)
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

### Report to User (Example Output)
```
💰 Currency: Euro (EUR)
   1 EUR = 1.00 EUR
   1 EUR = 1.08 USD
   1 EUR = 0.86 GBP
   1 EUR = 0.97 CHF
```

## 3. Acceptance Criteria (from the QA)
- The `money` command returns the official currency of the user's country and exchange rates to EUR, USD, GBP, and CHF.
- Output is structured, readable, and matches the format in the PRD examples.
- If the currency API, exchange rate API, or geolocation API is unavailable, the CLI shows a clear, friendly error message.
- The command responds within 3 seconds under normal network conditions.
- The CLI can be run from the terminal without errors or crashes.