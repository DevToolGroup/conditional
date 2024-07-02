package group.devtool.conditional.engine;

import java.util.List;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class AddFunction implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "ADD";
  }

  @Override
  public Void apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("ADD函数需要两个参数");
    }
    Object target = args[0];
    Object value = args[1];

    if (!(target instanceof List)) {
      throw RuleInstanceException.functionException("ADD函数赋值操作异常，参数类型必须为List");
    } 
    @SuppressWarnings("unchecked")
    List<Object> object = (List<Object>) target;
    object.add(value);

    return null;
  }

}
