package group.devtool.conditional.engine;

/**
 * 基于RETE算法实现的规则定义加载器
 */
public class ReteRuleClassLoader extends AbstractRuleClassLoader {

  private String id;

  public ReteRuleClassLoader(String id, String dl) {
    super(dl);
    this.id = id;
  }

  @Override
  protected AbstractRuleClass buildRuleClass() {
    return new ReteRuleClass(id);
  }
  
}
