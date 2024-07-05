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

import java.util.ArrayList;
import java.util.List;
import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

/**
 * Map初始化
 */
public class ListFunction implements ConditionFunction<List<Object>> {

  @Override
  public String getName() {
    return "LIST";
  }

  @Override
  public List<Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length < 1) {
      throw RuleInstanceException.functionException("LIST函数至少提供一个参数");
    }
    List<Object> result = new ArrayList<>();
    for (int i = 0; i < args.length; i += 1) {
      result.add(args[i]);
    }
    return result;
  }

}
