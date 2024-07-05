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

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class AddFunction implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "ADD";
  }

  @Override
  public Void apply(Object... args) throws RuleInstanceFunctionException {
    if (null == args || args.length != 2) {
      throw RuleInstanceException.functionException("ADD函数需要两个参数");
    }
    Object target = args[0];
    Object value = args[1];

    if (!(target instanceof List)) {
      throw RuleInstanceException.functionException("ADD函数赋值操作异常，参数类型必须为List");
    } 
    @SuppressWarnings("unchecked")
    List<Object> object = (List<Object>) target;
    object.add(value);

    return null;
  }

}
