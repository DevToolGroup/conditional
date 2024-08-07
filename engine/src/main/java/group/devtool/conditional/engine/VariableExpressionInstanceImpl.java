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

import java.lang.reflect.Field;
import java.util.Map;

/**
 * {@link VariableExpressionInstance} 默认实现
 * 
 * 说明：嵌套MAP结构的取值如：Map<Map<>, Map<>>目前无法支持！
 * 
 */
public class VariableExpressionInstanceImpl implements VariableExpressionInstance {

  private String name;

  public VariableExpressionInstanceImpl(String name) {
    this.name = name;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object target = context.peek();
    if (target instanceof Map) {
      Map<?, ?> mt = (Map<?, ?>) target;
      return mt.get(name);
    }

    // bean utils get
    Field field;
    try {
      field = target.getClass().getDeclaredField(name);
      return field.get(target);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw RuleInstanceException.valueResolveException("变量取值不存在");
    }
  }

  @Override
  public String getExpressionString() {
    return name;
  }

  @Override
  public String getName() {
    return name;
  }

}
