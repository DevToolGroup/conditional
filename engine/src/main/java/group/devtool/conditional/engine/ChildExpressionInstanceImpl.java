package group.devtool.conditional.engine;

/**
 * 嵌套表达式实例默认实现
 */
public class ChildExpressionInstanceImpl implements ChildExpressionInstance {

  private ExpressionInstance child;

  public ChildExpressionInstanceImpl(ExpressionInstance child) {
    this.child = child;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    return this.child.getCacheObject(context);
  }

  @Override
  public String getExpressionString() {
    return "(" + child.getExpressionString() + ")";
  }

  @Override
  public ExpressionInstance getChild() {
    return child;
  }

}
