package com.inetum.askbot;

import java.util.Map;

/**
 * Immutable record representing the response from the IP API.
 * Provides a fromJson factory that relies on the minimal JsonParser implemented here.
 */
public record IpApiResponse(
    String ip,
    String network,
    String version,
    String city,
    String region,
    String region_code,
    String country,
    String country_name,
    String country_code,
    String country_code_iso3,
    String country_capital,
    String country_tld,
    String continent_code,
    boolean in_eu,
    String postal,
    double latitude,
    double longitude,
    String timezone,
    String utc_offset,
    String country_calling_code,
    String currency,
    String currency_name,
    String languages,
    double country_area,
    long country_population,
    String asn,
    String org) {

  public static IpApiResponse fromJson(String json) {
    Map<String, Object> parsed = new JsonParser(json).parseObject();
    return new IpApiResponse(
        (String) parsed.get("ip"),
        (String) parsed.get("network"),
        (String) parsed.get("version"),
        (String) parsed.get("city"),
        (String) parsed.get("region"),
        (String) parsed.get("region_code"),
        (String) parsed.get("country"),
        (String) parsed.get("country_name"),
        (String) parsed.get("country_code"),
        (String) parsed.get("country_code_iso3"),
        (String) parsed.get("country_capital"),
        (String) parsed.get("country_tld"),
        (String) parsed.get("continent_code"),
        parsed.get("in_eu") == Boolean.TRUE,
        (String) parsed.get("postal"),
        (parsed.get("latitude") instanceof Number) ? ((Number) parsed.get("latitude")).doubleValue() : 0.0,
        (parsed.get("longitude") instanceof Number) ? ((Number) parsed.get("longitude")).doubleValue() : 0.0,
        (String) parsed.get("timezone"),
        (String) parsed.get("utc_offset"),
        (String) parsed.get("country_calling_code"),
        (String) parsed.get("currency"),
        (String) parsed.get("currency_name"),
        (String) parsed.get("languages"),
        (parsed.get("country_area") instanceof Number) ? ((Number) parsed.get("country_area")).doubleValue() : 0.0,
        (parsed.get("country_population") instanceof Number) ? ((Number) parsed.get("country_population")).longValue() : 0L,
        (String) parsed.get("asn"),
        (String) parsed.get("org"));
  }
}
