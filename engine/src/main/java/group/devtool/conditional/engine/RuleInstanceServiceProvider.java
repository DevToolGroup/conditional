package group.devtool.conditional.engine;


public class RuleInstanceServiceProvider {
  
  private RuleInstanceService service;

  public RuleInstanceServiceProvider(RuleClassService ruleClassService) {
    this.service = new ReteRuleInstanceService(ruleClassService);
  }

  public RuleInstanceServiceProvider(RuleInstanceService service) {
    this.service = service;
  }

  public RuleInstanceService get() {
    return service;
  }

}
