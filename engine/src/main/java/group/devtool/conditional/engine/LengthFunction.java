package group.devtool.conditional.engine;

import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class LengthFunction implements ConditionFunction<Integer> {

  @Override
  public String getName() {
    return "LEN";
  }

  @Override
  public Integer apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("LEN函数需要一个字符串，列表，字典参数");
    }
    Object target = args[0];
    if (target instanceof String) {
      return ((String) target).length();
    } else if (target instanceof List) {
      return ((List<?>) target).size();
    } else if (target instanceof Map) {
      return ((Map<?, ?>) target).size();
    } else {
      throw RuleInstanceException.functionException("LEN函数需要一个字符串，列表，字典参数");
    }
  }

}
