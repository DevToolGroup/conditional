package group.devtool.conditional.engine;

/**
 * 全局变量定义
 */
public interface VariableClass {

  /**
   * @return 变量类型
   */
  public String getType();

  /**
   * @return 变量编码
   */
  public String getCode();

  /**
   * @return 变量名称
   */
  public String getName();

  /**
   * @return 变量值表达式
   */
  public ExpressionClass getValueExpression();
}
