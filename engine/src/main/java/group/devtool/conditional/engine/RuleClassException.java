package group.devtool.conditional.engine;

public class RuleClassException extends Exception {

  public RuleClassException(String message) {
    super(message);
  }

  public static class RuleClassStreamException extends RuleClassException {

    public RuleClassStreamException(String message) {
      super(message);
    }
  
  }

  public static class RuleClassSyntaxException extends RuleClassException {

    private Integer pos;

    public RuleClassSyntaxException(int pos, CharSequence pattern) {
      super("语法错误。" + "位置：" + pos + " 字符" + pattern);
      this.pos = pos;
    }

    public RuleClassSyntaxException(String message) {
      super(message);
    }

    public Integer getPos() {
      return pos;
    }
  }

  public static RuleClassException streamException(String message) {
    return new RuleClassStreamException(message);
  }

  public static RuleClassException syntaxException(String message) {
    return new RuleClassSyntaxException(message);
  }

  public static RuleClassException syntaxException(int pos, CharSequence pattern) {
    return new RuleClassSyntaxException(pos, pattern);
  }

}
