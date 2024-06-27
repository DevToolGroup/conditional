package group.devtool.conditional.engine;

/**
 * 字符串
 */
public class StringExpressionInstanceImpl implements StringExpressionInstance {

  private String value;

  public StringExpressionInstanceImpl(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      this.value = value.substring(1, value.length() - 1);
    } else {
      this.value = value;
    }
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    return value;
  }

  @Override
  public String getExpressionString() {
    return "\"" + value + "\"";
  }

}
