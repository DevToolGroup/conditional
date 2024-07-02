package group.devtool.conditional.engine;

/**
 * {@link VariableClass} 实现类
 */
public class VariableClassImpl implements VariableClass {

  private String type;

  private String code;

  private String name;

  private ExpressionClass valueExpression;

  private String keyType;

  private String valueType;

  public VariableClassImpl(String type, String keyType, String valueType, String code, String name, ExpressionClass valueExpression)
      throws RuleClassException {
    this.type = type;
    this.keyType = keyType;
    this.valueType = valueType;
    this.code = code;
    this.name = name;
    this.valueExpression = valueExpression;
    valid();
  }

  private void valid() throws RuleClassException {
    ExpressionInstance instance = valueExpression.getInstance();
    DataType dt = DataType.valueOf(type);
    if (null == dt) {
      return;
    }
    if (instance instanceof LogicExpressionInstance
        || instance instanceof CompareExpressionInstance) {
      if (dt != DataType.Boolean) {
        throw RuleClassException.syntaxException("仅布尔类型变量支持逻辑表达式和比较表达式");
      }
    } else if (instance instanceof StringExpressionInstance) {
      if (dt != DataType.String) {
        throw RuleClassException.syntaxException("仅字符串类型支持字符串表达式");
      }
    } else if (instance instanceof NumberExpressionInstance) {
      if (!Number.class.isAssignableFrom(dt.getType())) {
        throw RuleClassException.syntaxException("仅字符串类型支持字符串表达式");
      }
    }
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getKeyType() {
    return keyType;
  }

  @Override
  public String getValueType() {
    return valueType;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ExpressionClass getValueExpression() {
    return valueExpression;
  }

}
