package group.devtool.conditional.engine;

import group.devtool.conditional.engine.Operation.Logic;

public interface LogicExpressionInstance extends ComposeExpressionInstance {

  public Logic getLogic();
  
}
