package group.devtool.conditional.engine;

import java.math.BigDecimal;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class RoundFunction implements ConditionFunction<Long> {

  @Override
  public String getName() {
    return "ROUND";
  }

  @Override
  public Long apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("ROUND函数只需要一个数字参数");
    }
    Object target = args[0];
    if (target instanceof Double) {
      return Math.round((Double) target);
    } else if (target instanceof Float) {
      return Long.valueOf(Math.round((Float) target));
    } else if (target instanceof BigDecimal) {
      return Long.valueOf(Math.round(((BigDecimal) target).doubleValue()));
    } else {
      throw RuleInstanceException.functionException("ROUND函数只需要一个数字参数");
    }
  }

}