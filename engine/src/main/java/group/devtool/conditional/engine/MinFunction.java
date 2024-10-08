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

import java.math.BigDecimal;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class MinFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "MIN";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("ABS函数只需要一个数字参数");
    }
    Object val = args[0];
    Object other = args[0];
    if (val instanceof Double) {
      return Math.min((Double) val, (Double) other);
    } else if (val instanceof Float) {
      return Math.min((Float) val, (Float) other);
    } else if (val instanceof BigDecimal) {
      return ((BigDecimal) val).min((BigDecimal) other);
    } else {
      throw RuleInstanceException.functionException("ROUND函数只需要一个数字参数");
    }
  }

}
