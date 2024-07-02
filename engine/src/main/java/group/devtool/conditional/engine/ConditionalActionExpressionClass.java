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
