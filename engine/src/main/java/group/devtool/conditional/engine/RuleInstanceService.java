package group.devtool.conditional.engine;

import java.util.Map;

/**
 * 规则实例服务
 */
public interface RuleInstanceService {

  /**
   * 根据规则定义ID，执行规则
   * 
   * @param ruleClassId 规则定义ID
   * @param params      执行参数
   * @param functions   自定义函数
   * @return 执行结果
   * @throws RuleInstanceException 规则实例运行异常
   * @throws RuleClassException    规则定义异常
   */
  public Map<String, Object> exec(String ruleClassId,
      Map<String, Object> params,
      ConditionFunction<?>... functions)
      throws RuleInstanceException, RuleClassException;

}
