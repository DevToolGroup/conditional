package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class TrimFunction implements ConditionFunction<String> {

  @Override
  public String getName() {
    return "TRIMS";
  }

  @Override
  public String apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("TRIMS函数需要一个字符串参数");
    }
    String target = (String) args[0];
    return target.trim();
  }

}
