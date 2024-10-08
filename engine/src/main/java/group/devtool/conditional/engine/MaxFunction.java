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

public class MaxFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "MAX";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("MAX函数需要两个数字参数");
    }
    Object val = args[0];
    Object other = args[1];
    if (val instanceof Double && other instanceof Double) {
      return Math.max((Double) val, (Double) other);
    } else if (val instanceof Float && other instanceof Float) {
      return Math.max((Float) val, (Float) other);
    } else if (val instanceof BigDecimal && other instanceof BigDecimal) {
      return ((BigDecimal) val).max((BigDecimal) other);
    } else {
      throw RuleInstanceException.functionException("MAX函数只需要一个数字参数");
    }
  }

}
