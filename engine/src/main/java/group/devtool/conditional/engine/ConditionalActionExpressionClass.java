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

/**
 * 规则行为表达式
 */
public class ConditionalActionExpressionClass extends GenericExpressionClass {

  private List<VariableReference> references;

  public ConditionalActionExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
    if (!(getInstance() instanceof FunctionExpressionInstance)) {
      throw RuleClassException.syntaxException("规则行文仅支持函数表达式");
    }
  }

  @Override
  public List<VariableReference> getVariableReference() {
    if (null == references) {
      references = new ArrayList<>();
    }
    return references;
  }
}
