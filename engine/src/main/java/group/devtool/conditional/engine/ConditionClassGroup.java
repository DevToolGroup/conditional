package group.devtool.conditional.engine;

import java.util.List;

/**
 * 规则组
 */
public interface ConditionClassGroup {

  /**
   * @return 单个规则条件
   */
  public List<ConditionClass> getConditions();

  /**
   * 添加规则条件到规则组，用于后续RETE网络的创建
   * 
   * @param conditionClass 规则条件
   */
  public void addCondition(ConditionClass conditionClass);

  /**
   * 规则
   */
  public void completed();

  public void invoke(RuleInstance instance) throws RuleInstanceException;

}
