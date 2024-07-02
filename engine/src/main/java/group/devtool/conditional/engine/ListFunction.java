package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * Map初始化
 */
public class ListFunction implements ConditionFunction<List<Object>> {

  @Override
  public String getName() {
    return "LIST";
  }

  @Override
  public List<Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length < 1) {
      throw RuleInstanceException.functionException("LIST函数至少提供一个参数");
    }
    List<Object> result = new ArrayList<>();
    for (int i = 0; i < args.length; i += 1) {
      result.add(args[i]);
    }
    return result;
  }

}
