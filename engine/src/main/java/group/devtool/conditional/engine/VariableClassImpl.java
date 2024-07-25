/*
 * The Conditional rule engine, similar to Drools, 
 * introduces the definition of input and output parameters, 
 * thereby demarcating the boundaries between programmers and business personnel. 
 * 
 * It reduces the complexity of rules, making it easier for business staff to maintain and use them.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
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

  public VariableClassImpl() {
  }

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

  public void setType(String type) {
    this.type = type;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValueExpression(ExpressionClass valueExpression) {
    this.valueExpression = valueExpression;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }
}
