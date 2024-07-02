package group.devtool.conditional.engine;

import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class ContainsFunction implements ConditionFunction<Boolean> {

  @Override
  public String getName() {
    return "IN";
  }

  @Override
  public Boolean apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("IN函数需要两个参数");
    }
    Object collection = args[0];
    Object element = args[1];

    if (collection instanceof String && element instanceof String) {
      return ((String)collection).contains((String)element);
    }
    if (collection instanceof List && element instanceof Object) {
      return ((List<?>)collection).contains(element);
    }
    if (collection instanceof Map && element instanceof Object) {
      return ((Map<?, ?>)collection).containsKey(element);
    }
    return false;
  }
  
}
