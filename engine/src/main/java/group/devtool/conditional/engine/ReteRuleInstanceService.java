package group.devtool.conditional.engine;

import java.util.Map;

/**
 * {@link RuleInstanceService} RETE规则实例服务
 */
class ReteRuleInstanceService implements RuleInstanceService {

  private RuleClassService classService;

  public ReteRuleInstanceService(RuleClassService classService) {
    this.classService = classService;
  }

  @Override
  public Map<String, Object> exec(String ruleClassId, Map<String, Object> params, ConditionFunction<?>... functions)
      throws RuleInstanceException, RuleClassException {
    RuleClass ruleClass = classService.loadRuleClass(ruleClassId);
    RuleInstance ruleInstance = buildRuleInstance(ruleClass);
    ruleInstance.initialized(params, functions);
    return ruleInstance.invoke();
  }

  private RuleInstance buildRuleInstance(RuleClass ruleClass) {
    return new ReteRuleInstance(ruleClass);
  }

}
