/*
 * The Conditional rule engine, similar to Drools, 
 * introduces the definition of input and output parameters, 
 * thereby demarcating the boundaries between programmers and business personnel. 
 * 
 * It reduces the complexity of rules, making it easier for business staff to maintain and use them.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
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
