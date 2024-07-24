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
 * 规则条件表达式合并为RETE树
 */
public class ReteConditionExpressionClass extends GenericExpressionClass {

  private List<VariableReference> references;

  public ReteConditionExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
    if (getInstance() instanceof StringExpressionInstance) {
      throw RuleClassException.syntaxException("规则条件仅支持变量，逻辑判断或者函数，不支持字符常量");
    }
    if (getInstance() instanceof NumberExpressionInstance) {
      throw RuleClassException.syntaxException("规则条件仅支持变量，逻辑判断或者函数，不支持数字常量");
    }
  }

  @Override
  public List<VariableReference> getVariableReferences() {
    if (null == references) {
      references = new ArrayList<>();
    }
    return references;
  }
}
