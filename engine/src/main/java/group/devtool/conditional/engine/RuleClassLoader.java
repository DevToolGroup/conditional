package group.devtool.conditional.engine;

/**
 * 规则定义加载器
 */
public interface RuleClassLoader {

  /**
   * 根据实际情况加载规则定义
   * 
   * @return 规则定义
   * @throws RuleClassException 规则定义异常
   */
  public RuleClass load() throws RuleClassException;

}
