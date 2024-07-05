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

import java.util.HashMap;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * Map初始化
 */
public class MapFunction implements ConditionFunction<Map<Object, Object>> {

  @Override
  public String getName() {
    return "MAP";
  }

  @Override
  public Map<Object, Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length % 2 != 0) {
      throw RuleInstanceException.functionException("MAP函数的参数个数需要是2的倍数");
    }
    Map<Object, Object> result = new HashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      result.put(args[i], args[i+1]);
    }
    return result;
  }

}
