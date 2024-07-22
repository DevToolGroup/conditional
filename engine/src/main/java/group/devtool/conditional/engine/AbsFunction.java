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

public class AbsFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "ABS";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("ABS函数只需要一个数字参数");
    }
    Object target = args[0];
    if (target instanceof Double) {
      return Math.abs((Double) target);
    } else if (target instanceof Float) {
      return Math.abs((Float) target);
    } else if (target instanceof BigDecimal) {
      return ((BigDecimal) target).abs();
    } else if (target instanceof Integer) {
      return Math.abs((Integer)target);
    } else if (target instanceof Long) {
      return Math.abs((Long)target);
    } else {
      throw RuleInstanceException.functionException("ABS函数只需要一个数字参数");
    }
  }

}
