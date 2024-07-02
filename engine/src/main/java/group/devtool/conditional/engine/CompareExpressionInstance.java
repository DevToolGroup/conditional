package group.devtool.conditional.engine;

import group.devtool.conditional.engine.Operation.Compare;

/**
 * 比较表达式实例
 */
public interface CompareExpressionInstance extends ComposeExpressionInstance {

  public Compare getCompare();
  
}
