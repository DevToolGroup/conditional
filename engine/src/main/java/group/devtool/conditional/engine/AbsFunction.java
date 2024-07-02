package group.devtool.conditional.engine;

import java.math.BigDecimal;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class AbsFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "ABS";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("ABS函数只需要一个数字参数");
    }
    Object target = args[0];
    if (target instanceof Double) {
      return Math.abs((Double) target);
    } else if (target instanceof Float) {
      return Math.abs((Float) target);
    } else if (target instanceof BigDecimal) {
      return Math.abs(((BigDecimal) target).doubleValue());
    } else {
      throw RuleInstanceException.functionException("ROUND函数只需要一个数字参数");
    }
  }

}
