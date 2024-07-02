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

  public static class TypeToken extends Token {

    private String type;

    private String valueType;
    
    private String keyType;

    public TypeToken(List<Character> value, TokenKind kind) throws RuleClassException {
      super(value, kind);
      resolve();
    }

    private void resolve() throws RuleClassException {
      String value = getValue();

      int begin = value.indexOf("<");
      int end = value.indexOf(">");

      if (begin != -1) {
        type = value.substring(0, begin).trim();
        String componentType = value.substring(begin + 1, end).trim();

        valueType = componentType;
        if (-1 != componentType.indexOf(",")) {
          String[] kv = componentType.split(",");
          if (kv.length != 2) {
            throw RuleClassException.syntaxException("参数类型定义异常。参数定义：" + type);
          }
          keyType = kv[0];
          valueType = kv[1];
        }
      } else {
        type = value;
      }
    }

    public String getType() {
      return type;
    }

    public String getValueType() {
      return valueType;
    }

    public String getKeyType() {
      return keyType;
    }

  }

}

