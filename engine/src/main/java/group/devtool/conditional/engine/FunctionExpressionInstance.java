package group.devtool.conditional.engine;

import java.util.List;

/**
 * 函数表达式实例
 */
public interface FunctionExpressionInstance extends ExpressionInstance {

  public String getFunctionName();

  public List<ExpressionInstance> getArguments();

}
