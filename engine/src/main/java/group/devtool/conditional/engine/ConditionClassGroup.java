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
 * 规则组
 */
public interface ConditionClassGroup extends Serializable {

  /**
   * @return 单个规则条件
   */
  public List<ConditionClass> getConditions();

  /**
   * 添加规则条件到规则组，用于后续RETE网络的创建
   * 
   * @param conditionClass 规则条件
   */
  public void addCondition(ConditionClass conditionClass);

  /**
   * 规则
   */
  public void completed();

  public void invoke(RuleInstance instance) throws RuleInstanceException;

}
