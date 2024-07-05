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
 * 嵌套表达式实例默认实现
 */
public class ChildExpressionInstanceImpl implements ChildExpressionInstance {

  private ExpressionInstance child;

  public ChildExpressionInstanceImpl(ExpressionInstance child) {
    this.child = child;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    return this.child.getCacheObject(context);
  }

  @Override
  public String getExpressionString() {
    return "(" + child.getExpressionString() + ")";
  }

  @Override
  public ExpressionInstance getChild() {
    return child;
  }

}
