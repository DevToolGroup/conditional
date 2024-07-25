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

/**
 * {@link VariableExpressionInstance} 属性嵌套访问变量实例
 */
public class NestVariableExpressionInstanceImpl implements VariableExpressionInstance {

  private ExpressionInstance getter;

  private VariableExpressionInstance childGetter;

  public NestVariableExpressionInstanceImpl(ExpressionInstance getter, VariableExpressionInstance childGetter) {
    this.getter = getter;
    this.childGetter = childGetter;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object obj = getter.getObject(context);
    if (obj == null) {
      return null;
    }
    context.push(obj);
    Object value = childGetter.getObject(context);
    context.pop();
    return value;
  }

  @Override
  public String getExpressionString() {
    return getter.getExpressionString() + childGetter.getExpressionString();
  }

  @Override
  public String getName() {
    return getter.getExpressionString();
  }

}
