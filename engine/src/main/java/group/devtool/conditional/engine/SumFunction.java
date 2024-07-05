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
import java.util.List;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class SumFunction implements ConditionFunction<Number> {

  @Override
  public String getName() {
    return "SUM";
  }

  @Override
  public Number apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 1) {
      throw RuleInstanceException.functionException("SUM函数需要一个列表类型参数，其元素类型为数值类型");
    }

    Object target = args[0];

    if (!(target instanceof List)) {
      throw RuleInstanceException.functionException("SUM函数需要一个列表类型参数");
    }

    Class<? extends Number> elementType = null;

    Integer intResult = 0;
    Double doubleResult = 0.0;
    Float floatResult = 0.0f;
    BigDecimal decimalResult = BigDecimal.ZERO;

    List<?> listTarget = (List<?>) target;
    for (Object object : listTarget) {
      if (!(object instanceof Number)) {
        throw RuleInstanceException.functionException("SUM函数列表元素类型非数值类型");
      }
      if (null != elementType && !object.getClass().equals(elementType)) {
        throw RuleInstanceException.functionException("SUM函数列表元素类型不一致");
      }
      Number number = (Number) object;

      if (object instanceof Integer) {
        intResult += number.intValue();

      } else if (object instanceof Double) {
        doubleResult += number.doubleValue();

      } else if (object instanceof Float) {
        floatResult += number.floatValue();

      } else if (object instanceof BigDecimal) {
        decimalResult = decimalResult.add((BigDecimal) object);

      } else {
        throw RuleInstanceException.functionException("SUM函数列表元素数值类型不支持");
      }

    }
    return intResult;
  }

}
