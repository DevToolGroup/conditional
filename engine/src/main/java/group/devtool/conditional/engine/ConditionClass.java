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

import java.io.Serializable;
import java.util.List;

/**
 * 规则条件定义
 */
public interface ConditionClass extends Serializable {

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
