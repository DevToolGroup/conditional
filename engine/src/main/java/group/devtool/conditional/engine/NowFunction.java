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

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class NowFunction implements ConditionFunction<Date> {

  @Override
  public String getName() {
    return "NOW";
  }

  @Override
  public Date apply(Object... args) throws RuleInstanceFunctionException {
    if (null != args && args.length != 0 || Arrays.stream(args).anyMatch(Objects::isNull)) {
      throw RuleInstanceException.functionException("NOW函数不需要参数");
    }
    return new Date();
  }

}
