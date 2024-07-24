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

/**
 * 规则定义加载器
 */
public interface RuleClassLoader {

  /**
   * 根据实际情况加载规则定义
   *
   * @param dl 规则定义文本
   * @return 规则定义
   * @throws RuleClassException 规则定义异常
   */
  public RuleClass load(String dl) throws RuleClassException;

}
