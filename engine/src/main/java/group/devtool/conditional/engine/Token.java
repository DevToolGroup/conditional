package group.devtool.conditional.engine;

import java.util.List;

public class Token {

  private String value;

  private TokenKind kind;

  public Token(List<Character> value, TokenKind kind) {
    this.value = build(value);
    this.kind = kind;
  }

  public Token(String value, TokenKind kind) {
    this.value = value;
    this.kind = kind;
  }

  private String build(List<Character> values) {
    StringBuilder builder = new StringBuilder();
    for (Character character : values) {
      builder.append(character);
    }
    return builder.toString();
  }

  public String getValue() {
    return value;
  }

  public TokenKind getKind() {
    return kind;
  }

}

