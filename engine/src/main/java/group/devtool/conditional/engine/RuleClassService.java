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
   * @throws RuleClassException 规则定义异常
   */
  public String addRuleClass(String ruleClass) throws RuleClassException;

  /**
   * 加载规则定义
   * 
   * @param ruleClassId 规则定义ID
   * @return 规则定义
   * @throws RuleClassException 规则定义异常
   */
  public RuleClass loadRuleClass(String ruleClassId) throws RuleClassException;

  /**
   * 失效规则定义
   * 
   * @param ruleClassId 规则定义ID
   * @throws RuleClassException 规则定义异常
   */
  public void expireRuleClass(String ruleClassId) throws RuleClassException;

}
