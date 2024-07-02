package group.devtool.conditional.engine;

import group.devtool.conditional.engine.Operation.Arith;

/**
 * 算术表达式实例
 */
public interface ArithExpressionInstance extends ComposeExpressionInstance {

  public Arith getArith();
  
}
