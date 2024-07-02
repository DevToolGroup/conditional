package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link FunctionExpressionInstance} 默认实现
 */
public class FunctionExpressionInstanceImpl implements FunctionExpressionInstance {

  private String funcName;

  private List<ExpressionInstance> arguments;

  public FunctionExpressionInstanceImpl(String funcName, List<ExpressionInstance> arguments) {
    this.funcName = funcName;
    this.arguments = arguments;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    ConditionFunction<?> function = context.getDeclaredFunction(funcName);
    return function.apply(getArgs(context));
  }

  private Object[] getArgs(RuleInstance context) throws RuleInstanceException {
    List<Object> result = new ArrayList<>();
    for (ExpressionInstance argument : arguments) {
      result.add(argument.getCacheObject(context));
    }
    return result.toArray();
  }

  @Override
  public String getExpressionString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < arguments.size(); i++) {
      builder.append(arguments.get(i).getExpressionString());
      if (i < arguments.size() - 1) {
        builder.append(",");
      }
    }
    return funcName + "(" + builder.toString() + ")";
  }

  @Override
  public String getFunctionName() {
    return funcName;
  }

  @Override
  public List<ExpressionInstance> getArguments() {
    return arguments;
  }

}
