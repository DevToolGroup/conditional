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

import java.util.Collection;

public class CollectionVariableExpressionInstanceImpl implements VariableExpressionInstance {

  private Integer name;

  public CollectionVariableExpressionInstanceImpl(String name) {
    this.name = Integer.parseInt(name);
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object value = context.peek();
    if (value == null) {
      return null;
    }
    if (value instanceof Collection) {
      return ((Collection<?>) value).toArray()[name];
    } else {
      throw RuleInstanceException.unexpectedException("预期数据类型为集合类型，实际类型：" + value.getClass().getSimpleName());
    }
  }

  @Override
  public String getExpressionString() {
    return "[" + name + "]";
  }

  @Override
  public String getName() {
    return String.valueOf(name);
  }

}
