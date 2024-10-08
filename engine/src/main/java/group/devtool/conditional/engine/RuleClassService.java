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
 * 规则定义服务，实现规则定义的新增，失效，加载
 */
public interface RuleClassService {

  /**
   * 加载规则定义
   *
   * @return 规则定义
   * @throws RuleClassException 规则定义异常
   */
  public RuleClass loadRuleClass() throws RuleClassException;

}
