package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * 规则函数
 */
public interface ConditionFunction<T> {

  /**
   * @return 函数名
   */
  public String getName();

  /**
   * 执行函数
   * 
   * @param args 函数参数
   * @return 函数结果
   * @throws RuleInstanceFunctionException 
   */
  public T apply(Object... args) throws RuleInstanceFunctionException;

}
