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

import java.util.List;

/**
 * {@link ConditionClass} 默认实现类
 */
public class ConditionClassImpl implements ConditionClass {

  private ExpressionClass condition;

  private List<ExpressionClass> actions;

  private Integer order;

  public ConditionClassImpl() {

  }

  public ConditionClassImpl(ExpressionClass condition, List<ExpressionClass> actions) {
    this.condition = condition;
    this.actions = actions;
  }

  @Override
  public ExpressionClass getCondition() {
    return condition;
  }

  public void setCondition(ExpressionClass condition) {
    this.condition = condition;
  }

  @Override
  public List<ExpressionClass> getFunctions() {
    return actions;
  }
  public void setFunctions(List<ExpressionClass> actions) {
    this.actions = actions;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  @Override
  public void setOrder(Integer order) {
    this.order = order;
  }

}
