package group.devtool.conditional.engine;

import java.util.Date;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class NowFunction implements ConditionFunction<Date> {

  @Override
  public String getName() {
    return "NOW";
  }

  @Override
  public Date apply(Object... args) throws RuleInstanceFunctionException {
    if (null != args && args.length != 0) {
      throw RuleInstanceException.functionException("NOW函数不需要参数");
    }
    return new Date();
  }

}
