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
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class FilterFunction implements ConditionFunction<List<Object>> {

  @Override
  public String getName() {
    return "FILTER";
  }

  @Override
  public List<Object> apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 3) {
      throw RuleInstanceException.functionException("FILTER函数需要一个列表类型参数，一个字符串属性参数，一个比较对象参数");
    }

    Object target = args[0];
    Object property = args[1];
    Object compare = args[2];

    if (!(target instanceof List) || !(property instanceof String)) {
      throw RuleInstanceException.functionException("FILTER函数需要一个列表类型参数，一个字符串属性参数，一个比较对象参数");
    }

    List<Object> result = new ArrayList<>();

    List<?> listTarget = (List<?>) target;
    String propertyName = (String) property;
    for (Object object : listTarget) {
      if (!(object instanceof Map)) {
        throw RuleInstanceException.functionException("FILTER函数列表元素类型非声明类型");
      }
      Map<?, ?> mapObject = (Map<?, ?>) object;
      if (mapObject.containsKey(propertyName) && mapObject.get(propertyName).equals(compare)) {
        result.add(object);
      }
    }
    return result;
  }

}
