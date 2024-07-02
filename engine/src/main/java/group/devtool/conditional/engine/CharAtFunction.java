package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class CharAtFunction implements ConditionFunction<Character> {

  @Override
  public String getName() {
    return "AT";
  }

  @Override
  public Character apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("AT函数需要一个字符串参数，一个位置参数");
    }
    Object target = args[0];
    Object index = args[1];
    if (target instanceof String && index instanceof Integer) {
      return ((String) target).charAt((Integer)index);
    } else {
      throw RuleInstanceException.functionException("LEN函数需要一个字符串，列表，字典参数");
    }
  }

}
