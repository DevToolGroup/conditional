package group.devtool.conditional.engine;

import java.util.List;

/**
 * 规则行为表达式
 */
public class ConditionalActionExpressionClass extends GenericExpressionClass {

  public ConditionalActionExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
    if (!(getInstance() instanceof FunctionExpressionInstance)) {
      throw RuleClassException.syntaxException("规则行文仅支持函数表达式");
    }
  }
}
