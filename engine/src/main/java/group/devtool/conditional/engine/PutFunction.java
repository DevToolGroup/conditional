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
import java.util.Map;
import java.util.Objects;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class PutFunction implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "PUT";
  }

  @Override
  public Void apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 3 || Arrays.stream(args).anyMatch(Objects::isNull)) {
      throw RuleInstanceException.functionException("PUT函数需要三个参数");
    }
    Object target = args[0];
    // 校验args[1] 是否是字符串类型
    if (!(args[1] instanceof String)) {
      throw RuleInstanceException.functionException("PUT函数赋值操作异常，参数类型必须为String");
    }
    String property = (String)args[1];
    Object value = args[2];

    if (!(target instanceof Map)) {
      throw RuleInstanceException.functionException("PUT函数赋值操作异常，参数类型必须为Map");
    } 
    @SuppressWarnings("unchecked")
    Map<Object, Object> object = (Map<Object, Object>) target;
    object.put(property, value);
    return null;
  }

}
