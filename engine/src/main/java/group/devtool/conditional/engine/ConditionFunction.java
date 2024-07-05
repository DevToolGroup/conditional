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

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * 规则函数
 */
public interface ConditionFunction<T> {

  /**
   * @return 函数名
   */
  public String getName();

  /**
   * 执行函数
   * 
   * @param args 函数参数
   * @return 函数结果
   * @throws RuleInstanceFunctionException 
   */
  public T apply(Object... args) throws RuleInstanceFunctionException;

}
