package group.devtool.conditional.engine;

/**
 * 数字 字面量表达式实例
 */
public interface NumberExpressionInstance extends ExpressionInstance {

  public Class<? extends Number> getType();
  
}
