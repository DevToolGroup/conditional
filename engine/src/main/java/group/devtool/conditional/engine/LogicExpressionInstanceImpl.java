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

import group.devtool.conditional.engine.Operation.Logic;

/**
 * 逻辑表达式实例
 */
public class LogicExpressionInstanceImpl implements LogicExpressionInstance {

  private ExpressionInstance left;

  private ExpressionInstance right;

  private Logic logic;

  public LogicExpressionInstanceImpl(ExpressionInstance left, ExpressionInstance right, Logic logic) {
    this.left = left;
    this.right = right;
    this.logic = logic;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    if (logic == Logic.NOT) {
      return notLogic(context);
    }
    if (logic == Logic.OR) {
      return orLogic(context);
    }
    if (logic == Logic.AND) {
      return andLogic(context);
    }
    throw RuleInstanceException.runtimeException("未知的逻辑操作");
  }

  private Object andLogic(RuleInstance context) throws RuleInstanceException {
    Object leftValue = left.getCacheObject(context);
    Object rightValue = right.getCacheObject(context);

    if (null == leftValue || null == rightValue) {
      return false;
    }

    if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
      return (Boolean)leftValue && (Boolean)rightValue;
    } else {
      throw RuleInstanceException.unexpectedException("语句执行结果不符合预期，预期类型：布尔类型");
    }
  }

  private Object orLogic(RuleInstance context) throws RuleInstanceException {
    Object leftValue = left.getCacheObject(context);
    Object rightValue = right.getCacheObject(context);

    if (null == leftValue || null == rightValue) {
        return false;
    }

    if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
      return (Boolean) leftValue || (Boolean) rightValue;
    } else {
      throw RuleInstanceException.unexpectedException("语句执行结果不符合预期，预期类型：布尔类型");
    }
  }

  private Object notLogic(RuleInstance context) throws RuleInstanceException {
    Object value = right.getCacheObject(context);
    if (null == value) {
      return true;
    }
    if (value instanceof Boolean) {
      return !(Boolean) value;
    } else {
      throw RuleInstanceException.unexpectedException("语句执行结果不符合预期，预期类型：布尔类型");
    }
  }

  @Override
  public ExpressionInstance left() {
    return left;
  }

  @Override
  public ExpressionInstance right() {
    return right;
  }

  @Override
  public String getExpressionString() {
    if (null == left) {
      return logic.op() + right.getExpressionString();
    }
    return left.getExpressionString() + logic.op() + right.getExpressionString();
  }

  @Override
  public Logic getLogic() {
    return logic;
  }

}
