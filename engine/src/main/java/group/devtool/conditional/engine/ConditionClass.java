package group.devtool.conditional.engine;

import java.util.List;

/**
 * 规则条件定义
 */
public interface ConditionClass {

  /**
   * @return 规则条件表达式
   */
  public ExpressionClass getCondition();

  /**
   * @return 规则动作表达式
   */
  public List<ExpressionClass> getFunctions();

  /**
   * @return 规则顺序
   */
  public Integer getOrder();

  /**
   * 设置规则顺序
   * 
   * @param order 规则顺序
   */
  public void setOrder(Integer order);

}
