import java.util.Map;

/**
 * Record inmutable que representa la respuesta de la API (sin dependencias).
 * Contiene fromJson que usa un parser JSON mínimo implementado aquí.
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
    Map<String, Object> m = new JsonParser(json).parseObject();
    return new IpApiResponse(
        (String) m.get("ip"),
        (String) m.get("network"),
        (String) m.get("version"),
        (String) m.get("city"),
        (String) m.get("region"),
        (String) m.get("region_code"),
        (String) m.get("country"),
        (String) m.get("country_name"),
        (String) m.get("country_code"),
        (String) m.get("country_code_iso3"),
        (String) m.get("country_capital"),
        (String) m.get("country_tld"),
        (String) m.get("continent_code"),
        m.get("in_eu") == Boolean.TRUE,
        (String) m.get("postal"),
        (m.get("latitude") instanceof Number) ? ((Number) m.get("latitude")).doubleValue() : 0.0,
        (m.get("longitude") instanceof Number) ? ((Number) m.get("longitude")).doubleValue() : 0.0,
        (String) m.get("timezone"),
        (String) m.get("utc_offset"),
        (String) m.get("country_calling_code"),
        (String) m.get("currency"),
        (String) m.get("currency_name"),
        (String) m.get("languages"),
        (m.get("country_area") instanceof Number) ? ((Number) m.get("country_area")).doubleValue() : 0.0,
        (m.get("country_population") instanceof Number) ? ((Number) m.get("country_population")).longValue() : 0L,
        (String) m.get("asn"),
        (String) m.get("org"));
  }

}
