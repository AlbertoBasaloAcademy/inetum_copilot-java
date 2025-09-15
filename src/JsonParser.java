import java.util.HashMap;
import java.util.Map;

public class JsonParser {
  private final String s;
  private int i = 0;

  JsonParser(String s) {
    this.s = s != null ? s : "";
  }

  Map<String, Object> parseObject() {
    skipWhitespace();
    if (!consumeIf('{'))
      throw error("Expected '{' at start of object");
    Map<String, Object> result = new HashMap<>();
    skipWhitespace();
    if (consumeIf('}'))
      return result; // empty object
    while (true) {
      skipWhitespace();
      String key = parseString();
      skipWhitespace();
      if (!consumeIf(':'))
        throw error("Expected ':' after key");
      skipWhitespace();
      Object value = parseValue();
      result.put(key, value);
      skipWhitespace();
      if (consumeIf('}'))
        break;
      if (!consumeIf(','))
        throw error("Expected ',' or '}'");
    }
    return result;
  }

  private Object parseValue() {
    skipWhitespace();
    if (peek() == '"')
      return parseString();
    char c = peek();
    if (c == '-' || (c >= '0' && c <= '9'))
      return parseNumber();
    if (startsWith("true")) {
      i += 4;
      return Boolean.TRUE;
    }
    if (startsWith("false")) {
      i += 5;
      return Boolean.FALSE;
    }
    if (startsWith("null")) {
      i += 4;
      return null;
    }
    throw error("Unexpected value at position " + i);
  }

  private String parseString() {
    if (!consumeIf('"'))
      throw error("Expected '\"' at start of string");
    StringBuilder sb = new StringBuilder();
    while (i < s.length()) {
      char ch = s.charAt(i++);
      if (ch == '"')
        return sb.toString();
      if (ch == '\\') {
        if (i >= s.length())
          throw error("Unterminated escape sequence");
        char esc = s.charAt(i++);
        switch (esc) {
          case '"':
            sb.append('"');
            break;
          case '\\':
            sb.append('\\');
            break;
          case '/':
            sb.append('/');
            break;
          case 'b':
            sb.append('\b');
            break;
          case 'f':
            sb.append('\f');
            break;
          case 'n':
            sb.append('\n');
            break;
          case 'r':
            sb.append('\r');
            break;
          case 't':
            sb.append('\t');
            break;
          case 'u':
            if (i + 4 > s.length())
              throw error("Invalid unicode escape");
            String hex = s.substring(i, i + 4);
            try {
              int code = Integer.parseInt(hex, 16);
              sb.append((char) code);
            } catch (NumberFormatException ex) {
              throw error("Invalid unicode escape: \\u" + hex);
            }
            i += 4;
            break;
          default:
            throw error("Invalid escape char: \\" + esc);
        }
      } else {
        sb.append(ch);
      }
    }
    throw error("Unterminated string");
  }

  private Number parseNumber() {
    int start = i;
    if (peek() == '-')
      i++;
    while (i < s.length() && Character.isDigit(s.charAt(i)))
      i++;
    boolean isDouble = false;
    if (i < s.length() && s.charAt(i) == '.') {
      isDouble = true;
      i++;
      while (i < s.length() && Character.isDigit(s.charAt(i)))
        i++;
    }
    if (i < s.length() && (s.charAt(i) == 'e' || s.charAt(i) == 'E')) {
      isDouble = true;
      i++;
      if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-'))
        i++;
      while (i < s.length() && Character.isDigit(s.charAt(i)))
        i++;
    }
    String num = s.substring(start, i);
    try {
      if (isDouble)
        return Double.parseDouble(num);
      else {
        try {
          return Long.parseLong(num);
        } catch (NumberFormatException ex) {
          return Double.parseDouble(num);
        }
      }
    } catch (NumberFormatException ex) {
      throw error("Invalid number: " + num);
    }
  }

  private void skipWhitespace() {
    while (i < s.length()) {
      char c = s.charAt(i);
      if (c == ' ' || c == '\n' || c == '\r' || c == '\t')
        i++;
      else
        break;
    }
  }

  private boolean consumeIf(char expected) {
    if (i < s.length() && s.charAt(i) == expected) {
      i++;
      return true;
    }
    return false;
  }

  private char peek() {
    return i < s.length() ? s.charAt(i) : '\0';
  }

  private boolean startsWith(String p) {
    return s.regionMatches(i, p, 0, p.length());
  }

  private IllegalArgumentException error(String msg) {
    return new IllegalArgumentException(msg + " (at pos " + i + ")");
  }
}
