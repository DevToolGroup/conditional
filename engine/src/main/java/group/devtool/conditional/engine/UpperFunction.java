package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class UpperFunction implements ConditionFunction<Character> {

  @Override
  public String getName() {
    return "UPPER";
  }

  @Override
  public Character apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("UPPER函数只需要一个字符串");
    }
    Object target = args[0];
    if (target instanceof String) {
      return Character.toUpperCase(((String) target).charAt(0));
    } else {
      throw RuleInstanceException.functionException("LEN函数需要一个字符串，列表，字典参数");
    }
  }

}
