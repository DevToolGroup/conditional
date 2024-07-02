package group.devtool.conditional.engine;

import java.math.BigDecimal;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class MaxFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "MAX";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("MAX函数只需要一个数字参数");
    }
    Object val = args[0];
    Object other = args[0];
    if (val instanceof Double && other instanceof Double) {
      return Math.max((Double) val, (Double) other);
    } else if (val instanceof Float && other instanceof Float) {
      return Math.max((Float) val, (Float) other);
    } else if (val instanceof BigDecimal && other instanceof BigDecimal) {
      return Math.max(((BigDecimal) val).doubleValue(), ((BigDecimal) other).doubleValue());
    } else {
      throw RuleInstanceException.functionException("ROUND函数只需要一个数字参数");
    }
  }

}