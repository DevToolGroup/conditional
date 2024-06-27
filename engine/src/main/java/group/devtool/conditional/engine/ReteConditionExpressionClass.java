package group.devtool.conditional.engine;

import java.util.List;

/**
 * 规则条件表达式合并为RETE树
 */
public class ReteConditionExpressionClass extends GenericExpressionClass {

  public ReteConditionExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
    if (getInstance() instanceof StringExpressionInstance) {
      throw RuleClassException.syntaxException("规则条件仅支持变量，逻辑判断或者函数，不支持字符常量");
    }
    if (getInstance() instanceof NumberExpressionInstance) {
      throw RuleClassException.syntaxException("规则条件仅支持变量，逻辑判断或者函数，不支持数字常量");
    }
  }
}
