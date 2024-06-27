package group.devtool.conditional.engine;

import java.util.List;

/**
 * {@link ConditionClass} 默认实现类
 */
public class ConditionClassImpl implements ConditionClass {

  private ExpressionClass condition;

  private List<ExpressionClass> actions;

  public ConditionClassImpl(ExpressionClass condition, List<ExpressionClass> actions) {
    this.condition = condition;
    this.actions = actions;
  }

  @Override
  public ExpressionClass getCondition() {
    return condition;
  }

  @Override
  public List<ExpressionClass> getFunctions() {
    return actions;
  }

}
