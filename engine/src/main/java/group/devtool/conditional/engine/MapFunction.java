package group.devtool.conditional.engine;

import java.util.HashMap;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * Map初始化
 */
public class MapFunction implements ConditionFunction<Map<Object, Object>> {

  @Override
  public String getName() {
    return "MAP";
  }

  @Override
  public Map<Object, Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length % 2 != 0) {
      throw RuleInstanceException.functionException("MAP函数的参数个数需要是2的倍数");
    }
    Map<Object, Object> result = new HashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      result.put(args[i], args[i+1]);
    }
    return result;
  }

}
