package group.devtool.conditional.engine;

/**
 * 组合规则实例
 */
public interface ComposeExpressionInstance extends ExpressionInstance {

  /**
   * @return 左部
   */
  public ExpressionInstance left();

  /**
   * @return 右部
   */
  public ExpressionInstance right();

}
