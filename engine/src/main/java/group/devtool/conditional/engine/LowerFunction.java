package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class LowerFunction implements ConditionFunction<Character> {

  @Override
  public String getName() {
    return "LOWER";
  }

  @Override
  public Character apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("LOWER函数只需要一个字符串");
    }
    Object target = args[0];
    if (target instanceof String) {
      return Character.toLowerCase(((String) target).charAt(0));
    } else {
      throw RuleInstanceException.functionException("LOWER函数只需要一个字符串");
    }
  }

}
