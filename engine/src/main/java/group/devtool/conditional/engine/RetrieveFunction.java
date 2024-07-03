package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class RetrieveFunction implements ConditionFunction<List<Object>> {

  @Override
  public String getName() {
    return "RETRIEVE";
  }

  @Override
  public List<Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("RETRIEVE函数需要一个列表类型参数，一个字符串属性参数");
    }
    Object target = args[0];
    Object property = args[1];
    if (!(target instanceof List) || !(property instanceof String)) {
      throw RuleInstanceException.functionException("RETRIEVE函数需要一个列表类型参数，一个字符串属性参数");
    }

    List<Object> result = new ArrayList<>();

    List<?> listTarget = (List<?>) target;
    String propertyName = (String) property;
    for (Object object : listTarget) {
      if (!(object instanceof Map)) {
        throw RuleInstanceException.functionException("RETRIEVE函数列表元素类型非声明类型");
      }
      Map<?, ?> mapObject = (Map<?, ?>) object;
      if (mapObject.containsKey(propertyName)) {
        result.add(mapObject.get(propertyName));
      }
    }
    return result;
  }

}
