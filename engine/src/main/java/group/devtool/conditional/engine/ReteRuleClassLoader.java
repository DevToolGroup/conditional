package group.devtool.conditional.engine;

import java.io.InputStream;

/**
 * 基于RETE算法实现的规则定义加载器
 */
public class ReteRuleClassLoader extends AbstractRuleClassLoader {

  private String id;

  public ReteRuleClassLoader(String id, InputStream inputStream) throws RuleClassException {
    super(inputStream);
    this.id = id;
  }

  @Override
  protected AbstractRuleClass buildRuleClass() {
    return new ReteRuleClass(id);
  }
  
}
