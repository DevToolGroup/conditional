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
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class ContainsFunction implements ConditionFunction<Boolean> {

  @Override
  public String getName() {
    return "IN";
  }

  @Override
  public Boolean apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("IN函数需要两个参数");
    }
    Object collection = args[0];
    Object element = args[1];

    if (collection instanceof String && element instanceof String) {
      return ((String)collection).contains((String)element);
    }
    if (collection instanceof List && element instanceof Object) {
      return ((List<?>)collection).contains(element);
    }
    if (collection instanceof Map && element instanceof Object) {
      return ((Map<?, ?>)collection).containsKey(element);
    }
    return false;
  }
  
}
