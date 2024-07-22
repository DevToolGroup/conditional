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

public class CharAtFunction implements ConditionFunction<Character> {

  @Override
  public String getName() {
    return "AT";
  }

  @Override
  public Character apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("AT函数需要一个字符串参数，一个位置参数");
    }
    Object target = args[0];
    Object index = args[1];
    if (target instanceof String && index instanceof Integer) {
      return ((String) target).charAt((Integer)index);
    } else {
      throw RuleInstanceException.functionException("AT函数需要一个字符串，列表，字典参数");
    }
  }

}
