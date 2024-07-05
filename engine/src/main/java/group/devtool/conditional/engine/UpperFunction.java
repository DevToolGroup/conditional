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

public class UpperFunction implements ConditionFunction<Character> {

  @Override
  public String getName() {
    return "UPPER";
  }

  @Override
  public Character apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("UPPER函数只需要一个字符串");
    }
    Object target = args[0];
    if (target instanceof String) {
      return Character.toUpperCase(((String) target).charAt(0));
    } else {
      throw RuleInstanceException.functionException("LEN函数需要一个字符串，列表，字典参数");
    }
  }

}
