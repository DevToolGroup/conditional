package group.devtool.conditional.engine;

import java.util.Map;

/**
 * {@link RuleInstanceService} RETE规则实例服务
 */
public class ReteRuleInstanceService implements RuleInstanceService {

  private RuleClassService classService;

  public ReteRuleInstanceService(RuleClassService classService) {
    this.classService = classService;
  }

  @Override
  public Map<String, Object> exec(String ruleClassId, Map<String, Object> params, ConditionFunction<?>... functions) throws RuleInstanceException {
    RuleClass ruleClass = classService.loadRuleClass(ruleClassId);
    RuleInstance ruleInstance = buildRuleInstance(ruleClass);

    Map<String, ConditionFunction<?>> builtin = loadBuiltInFunction();
    if (null != functions && functions.length > 0) {
      for (ConditionFunction<?> function : functions) {
        builtin.put(function.getName(), function);
      }
    }
    ruleInstance.initialized(params, functions);
    return ruleInstance.invoke();
  }

  private RuleInstance buildRuleInstance(RuleClass ruleClass) {
    return new ReteRuleInstance(ruleClass);
  }

  private Map<String, ConditionFunction<?>> loadBuiltInFunction() {
    return Functions.toMap();
  }

}
