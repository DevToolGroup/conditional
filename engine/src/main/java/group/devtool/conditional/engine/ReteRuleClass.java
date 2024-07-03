package group.devtool.conditional.engine;

class ReteRuleClass extends AbstractRuleClass {

  private ConditionClassGroup conditionGroup;

  public ReteRuleClass(String id) {
    super(id);
    this.conditionGroup = new ReteConditionClassGroup();
  }

  @Override
  public ConditionClassGroup getConditionGroup() {
    return conditionGroup;
  }
  
}
