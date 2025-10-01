package com.inetum.askbot;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
  private final String input;
  private int index = 0;

  /**
   * Constructs a new {@code JsonParser} with the specified input string.
   * If the provided string is {@code null}, it initializes the parser with an
   * empty string.
   *
   * @param input the JSON string to be parsed, or {@code null} to use an empty string
   */
  JsonParser(String input) {
    this.input = input != null ? input : "";
  }

  Map<String, Object> parseObject() {
    skipWhitespace();
    if (!consumeIf('{')) {
      throw error("Expected '{' at start of object");
    }
    Map<String, Object> result = new HashMap<>();
    skipWhitespace();
    if (consumeIf('}')) {
      return result; // empty object
    }
    while (true) {
      skipWhitespace();
      String key = parseString();
      skipWhitespace();
      if (!consumeIf(':')) {
        throw error("Expected ':' after key");
      }
      skipWhitespace();
      Object value = parseValue();
      result.put(key, value);
      skipWhitespace();
      if (consumeIf('}')) {
        break;
      }
      if (!consumeIf(',')) {
        throw error("Expected ',' or '}'");
      }
    }
    return result;
  }

  private Object parseValue() {
    skipWhitespace();
    if (peek() == '"') {
      return parseString();
    }
    char c = peek();
    if (c == '-' || (c >= '0' && c <= '9')) {
      return parseNumber();
    }
    if (startsWith("true")) {
      index += 4;
      return Boolean.TRUE;
    }
    if (startsWith("false")) {
      index += 5;
      return Boolean.FALSE;
    }
    if (startsWith("null")) {
      index += 4;
      return null;
    }
    throw error("Unexpected value at position " + index);
  }

  private String parseString() {
    if (!consumeIf('"')) {
      throw error("Expected '\"' at start of string");
    }
    StringBuilder sb = new StringBuilder();
    while (index < input.length()) {
      char ch = input.charAt(index++);
      if (ch == '"') {
        return sb.toString();
      }
      if (ch == '\\') {
        if (index >= input.length()) {
          throw error("Unterminated escape sequence");
        }
        char esc = input.charAt(index++);
        switch (esc) {
          case '"' -> sb.append('"');
          case '\\' -> sb.append('\\');
          case '/' -> sb.append('/');
          case 'b' -> sb.append('\b');
          case 'f' -> sb.append('\f');
          case 'n' -> sb.append('\n');
          case 'r' -> sb.append('\r');
          case 't' -> sb.append('\t');
          case 'u' -> {
            if (index + 4 > input.length()) {
              throw error("Invalid unicode escape");
            }
            String hex = input.substring(index, index + 4);
            try {
              int code = Integer.parseInt(hex, 16);
              sb.append((char) code);
            } catch (NumberFormatException ex) {
              throw error("Invalid unicode escape: \\u" + hex);
            }
            index += 4;
          }
          default -> throw error("Invalid escape char: \\" + esc);
        }
      } else {
        sb.append(ch);
      }
    }
    throw error("Unterminated string");
  }

  private Number parseNumber() {
    int start = index;
    if (peek() == '-') {
      index++;
    }
    while (index < input.length() && Character.isDigit(input.charAt(index))) {
      index++;
    }
    boolean isDouble = false;
    if (index < input.length() && input.charAt(index) == '.') {
      isDouble = true;
      index++;
      while (index < input.length() && Character.isDigit(input.charAt(index))) {
        index++;
      }
    }
    if (index < input.length() && (input.charAt(index) == 'e' || input.charAt(index) == 'E')) {
      isDouble = true;
      index++;
      if (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
        index++;
      }
      while (index < input.length() && Character.isDigit(input.charAt(index))) {
        index++;
      }
    }
    String number = input.substring(start, index);
    try {
      if (isDouble) {
        return Double.parseDouble(number);
      }
      try {
        return Long.parseLong(number);
      } catch (NumberFormatException ex) {
        return Double.parseDouble(number);
      }
    } catch (NumberFormatException ex) {
      throw error("Invalid number: " + number);
    }
  }

  private void skipWhitespace() {
    while (index < input.length()) {
      char c = input.charAt(index);
      if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
        index++;
      } else {
        break;
      }
    }
  }

  private boolean consumeIf(char expected) {
    if (index < input.length() && input.charAt(index) == expected) {
      index++;
      return true;
    }
    return false;
  }

  private char peek() {
    return index < input.length() ? input.charAt(index) : '\0';
  }

  private boolean startsWith(String pattern) {
    return input.regionMatches(index, pattern, 0, pattern.length());
  }

  private IllegalArgumentException error(String message) {
    return new IllegalArgumentException(message + " (at pos " + index + ")");
  }
}
