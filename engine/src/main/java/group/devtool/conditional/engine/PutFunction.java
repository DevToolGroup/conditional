package group.devtool.conditional.engine;

import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class PutFunction implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "PUT";
  }

  @Override
  public Void apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 3) {
      throw RuleInstanceException.functionException("PUT函数需要三个参数");
    }
    Object target = args[0];
    String property = (String)args[1];
    Object value = args[2];

    if (!(target instanceof Map)) {
      throw RuleInstanceException.functionException("PUT函数赋值操作异常，参数类型必须为Map");
    } 
    @SuppressWarnings("unchecked")
    Map<Object, Object> object = (Map<Object, Object>) target;
    object.put(property, value);
    return null;
  }

}
