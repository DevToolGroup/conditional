package group.devtool.conditional.engine;

import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class SubFunction implements ConditionFunction<String> {

  @Override
  public String getName() {
    return "SUBS";
  }

  @Override
  public String apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 3) {
      throw RuleInstanceException.functionException("SUBS函数需要一个字符串参数，一个起始参数，一个截止参数");
    }
    String target = (String) args[0];
    Integer begin = (Integer) args[1];
    Integer end = (Integer) args[2];
    return target.substring(begin, end);
  }

}
