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
package group.devtool.conditional.documentation;

import group.devtool.conditional.engine.ConditionFunction;
import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;

public class CustomFunctionExample implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "CUS";
  }

  @Override
  public Void apply(Object... args) throws RuleInstanceFunctionException {
    System.out.println("custom function");
    return null;
  }

}
