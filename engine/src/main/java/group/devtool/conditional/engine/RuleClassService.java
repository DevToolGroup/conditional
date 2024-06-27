package group.devtool.conditional.engine;

/**
 * 规则定义服务，实现规则定义的新增，失效，加载
 */
public interface RuleClassService {

  /**
   * 新增规则定义
   * 
   * @param ruleClass 规则定义字符串
   * @return
   */
  public String addRuleClass(String ruleClass);

  /**
   * 加载规则定义
   * 
   * @param ruleClassId 规则定义ID
   * @return 规则定义
   */
  public RuleClass loadRuleClass(String ruleClassId);

  /**
   * 失效规则定义
   * 
   * @param ruleClassId 规则定义ID
   */
  public void expireRuleClass(String ruleClassId);

}
