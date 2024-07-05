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

public class SubFunction implements ConditionFunction<String> {

  @Override
  public String getName() {
    return "SUBS";
  }

  @Override
  public String apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 3) {
      throw RuleInstanceException.functionException("SUBS函数需要一个字符串参数，一个起始参数，一个截止参数");
    }
    String target = (String) args[0];
    Integer begin = (Integer) args[1];
    Integer end = (Integer) args[2];
    return target.substring(begin, end);
  }

}
