package com.inetum.askbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JsonParser")
class JsonParserTest {

  @Test
  @DisplayName("parseObject should read primitives when JSON is valid")
  void parseObject_shouldReadPrimitiveFields_whenJsonIsValid() {
    // Arrange
    String json = "{\"name\":\"Ask Bot\",\"active\":true,\"count\":42}";
    JsonParser parser = new JsonParser(json);

    // Act
    Map<String, Object> result = parser.parseObject();

    // Assert
    assertEquals("Ask Bot", result.get("name"));
    assertEquals(Boolean.TRUE, result.get("active"));
    assertEquals(42L, result.get("count"));
  }

  @Test
  @DisplayName("parseObject should fail fast when braces are missing")
  void parseObject_shouldThrow_whenJsonMissingOpeningBrace() {
    // Arrange
    String invalidJson = "\"name\":\"Ask Bot\"}";
    JsonParser parser = new JsonParser(invalidJson);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, parser::parseObject);
  }
}
