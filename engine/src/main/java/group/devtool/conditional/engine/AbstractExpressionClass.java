package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.List;

import group.devtool.conditional.engine.Operation.Arith;
import group.devtool.conditional.engine.Operation.Compare;
import group.devtool.conditional.engine.Operation.Logic;

/**
 * {@link ExpressionClass 抽象实现}
 */
public abstract class AbstractExpressionClass implements ExpressionClass {

  private List<Token> tokens;

  private ExpressionInstance instance;

  private int pos;

  private int max;

  public AbstractExpressionClass(List<Token> tokens) throws RuleClassException {
    this.tokens = tokens;
    this.max = tokens.size();
    this.instance = logicParse();
    if (null != next()) {
      throw RuleClassException.syntaxException(pos, tokens.get(pos).getValue());
    }
  }

  @Override
  public ExpressionInstance getInstance() {
    return instance;
  }

  /**
   * 表达式语法解析。
   * 
   * 表达式语法解析，按照表达式中各个元素的优先级依次解析。
   * 
   * @return 表达式实例
   * @throws RuleClassException 规则定义异常
   */
  private ExpressionInstance logicParse() throws RuleClassException {
    ExpressionInstance left = logicAndParse();
    if (next() == TokenKind.OR) {
      pos += 1;
      Token token = tokens.get(pos);
      pos += 1;
      return buildLogicExpressionInstance(left, logicParse(), Logic.get(token.getValue()));
    }
    return left;
  }

  /**
   * 构造 逻辑 表达式
   * 
   * 这里之所以做成抽象方法，是为了接下来构造rete网络准备的，
   * 同时，考虑到后续的场景，可能采用解释模式而非rete模式。
   * 
   * @param left  左 表达式
   * @param right 右 表达式
   * @param logic 运算符
   * @return 逻辑 表达式
   */
  protected abstract ExpressionInstance buildLogicExpressionInstance(ExpressionInstance left, ExpressionInstance right,
      Logic logic);

  private ExpressionInstance logicAndParse() throws RuleClassException {
    ExpressionInstance left = logicNotParse();
    if (next() == TokenKind.AND) {
      pos += 1;
      Token token = tokens.get(pos);
      pos += 1;
      return buildLogicExpressionInstance(left, logicAndParse(), Logic.get(token.getValue()));
    }
    return left;
  }

  private ExpressionInstance logicNotParse() throws RuleClassException {
    if (next() == TokenKind.NOT) {
      pos += 1;
      Token token = tokens.get(pos);
      pos += 1;
      return buildLogicExpressionInstance(null, logicParse(), Logic.get(token.getValue()));
    }
    return compareParse();
  }

  private ExpressionInstance compareParse() throws RuleClassException {
    ExpressionInstance left = arithParse();
    if (next() == TokenKind.GT
        || next() == TokenKind.GE
        || next() == TokenKind.LT
        || next() == TokenKind.LE
        || next() == TokenKind.EQ
        || next() == TokenKind.NE) {
      pos += 1;
      Compare operation = Compare.get(tokens.get(pos).getValue());
      pos += 1;
      return buildCompareExpressionInstance(left, operation, arithParse());
    }
    return left;
  }

  /**
   * 构造 判断 表达式
   * 
   * @param left  左 表达式
   * @param right 右 表达式
   * @return 判断 表达式
   */
  protected abstract ExpressionInstance buildCompareExpressionInstance(ExpressionInstance left, Compare operation,
      ExpressionInstance valueParse);

  private ExpressionInstance arithParse() throws RuleClassException {
    ExpressionInstance left = valueParse();
    if (next() == TokenKind.PLUS
        || next() == TokenKind.MINUS
        || next() == TokenKind.MUL
        || next() == TokenKind.DIV
        || next() == TokenKind.MOD
        || next() == TokenKind.POWER) {
      pos += 1;
      Token token = tokens.get(pos);
      pos += 1;
      return buildArithExpression(left, Arith.get(token.getValue()), valueParse());
    }
    return left;
  }

  /**
   * 构造 算术 表达式
   * 
   * @param left  左 表达式
   * @param right 右 表达式
   * @param arith 算术运算符
   * @return 算术 表达式
   */
  protected abstract ExpressionInstance buildArithExpression(ExpressionInstance left,
      Arith arith,
      ExpressionInstance valueParse);

  private ExpressionInstance valueParse() throws RuleClassException {
    Token token = tokens.get(pos);
    if (token.getKind() == TokenKind.LPAREN) {
      pos += 1;
      ExpressionInstance child = buildChildExpressionInstance(logicParse());
      pos += 1;
      return child;
    }
    if (token.getKind() == TokenKind.STRING) {
      return buildStringExpressionInstance(token.getValue());
    }
    if (token.getKind() == TokenKind.NUMBER) {
      return buildNumberExpressionInstance(token.getValue());
    }
    if (token.getKind() == TokenKind.VAR) {
      if (next() == TokenKind.DOT || next() == TokenKind.LBRACKET) {
        return getVariable();

      } else if (next() == TokenKind.LPAREN) {
        return getFunction();

      } else {
        return buildVariableExpressionInstance(token.getValue(), false);
      }
    }
    throw new RuntimeException();
  }

  /**
   * 构造 嵌套 表达式，即：（嵌套表达式）
   * 
   * @param logicParse 嵌套表达式
   * @return 嵌套 表达式
   */
  protected abstract ExpressionInstance buildChildExpressionInstance(ExpressionInstance logicParse);

  /**
   * 构造 字符串字面量 表达式
   * 
   * @param value 字符串
   * @return 字符串字面量表达式
   */
  protected abstract ExpressionInstance buildStringExpressionInstance(String value);

  /**
   * 构造 数字字面量 表达式
   * 
   * @param value 字符串
   * @return 数字字面量表达式
   * @throws RuleClassException
   * @throws RuleInstanceException
   */
  protected abstract ExpressionInstance buildNumberExpressionInstance(String value) throws RuleClassException;

  /**
   * 构造 变量引用 表达式
   * 
   * @param value   变量
   * @param hasNest 是否存在嵌套变量
   * @return 变量 表达式
   */
  protected abstract VariableExpressionInstance buildVariableExpressionInstance(String value, boolean hasNest);

  private ExpressionInstance getFunction() throws RuleClassException {
    String funcName = tokens.get(pos).getValue();
    pos += 2;
    List<ExpressionInstance> arguments = new ArrayList<>();
    // 跳过 （
    boolean loop = true;
    do {
      if (tokens.get(pos).getKind() == TokenKind.RPAREN) {
        break;
      }
      ExpressionInstance expression = logicParse();
      arguments.add(expression);
      if (next() == TokenKind.COMMA) {
        pos += 2;
        continue;
      }
      if (next() != TokenKind.RPAREN) {
        throw RuleClassException.syntaxException(pos + 1, "函数定义异常，函数名：" + funcName);
      }
      pos += 1;
    } while (pos < max && loop);

    if (pos >= max) {
      throw RuleClassException.syntaxException("方法调用表达式解析异常");
    }
    return buildFunctionExpressionInstance(funcName, arguments);
  }

  /**
   * 构造 方法调用 表达式
   * 
   * @param funcName  函数名称
   * @param arguments 参数列表
   * @return 方法调用 表达式
   */
  protected abstract FunctionExpressionInstance buildFunctionExpressionInstance(String funcName,
      List<ExpressionInstance> arguments);

  private ExpressionInstance getVariable() throws RuleClassException {
    if (pos >= max) {
      return null;
    }
    Token varToken = tokens.get(pos);
    VariableExpressionInstance child = getChildVariable();
    if (null != child) {
      VariableExpressionInstance first = buildVariableExpressionInstance(varToken.getValue(), true);
      return buildNestVariableExpressionInstance(first, child);
    } else {
      return buildVariableExpressionInstance(varToken.getValue(), false);
    }
  }

  /**
   * 构造 嵌套变量引用 表达式
   * 
   * @param first 变量
   * @param child 嵌套变量
   * @return 嵌套变量 表达式
   */
  protected abstract VariableExpressionInstance buildNestVariableExpressionInstance(VariableExpressionInstance first,
      VariableExpressionInstance child);

  public VariableExpressionInstance getChildVariable() throws RuleClassException {
    if (pos >= max) {
      return null;
    }
    if (next() == TokenKind.DOT) {
      pos += 2;
      return getPropertyVariable();
    }
    if (next() == TokenKind.LBRACKET) {
      pos += 2;
      return getCollectionVariable();
    }
    return null;
  }

  private VariableExpressionInstance getPropertyVariable() throws RuleClassException {
    if (pos >= max) {
      throw RuleClassException.syntaxException(pos, "变量取值语法错误");
    }
    String name = tokens.get(pos).getValue();
    VariableExpressionInstance first = buildPropertyVariableExpressionInstance(name);

    VariableExpressionInstance child = getChildVariable();
    if (null != child) {
      return buildNestVariableExpressionInstance(first, child);
    }
    return first;
  }

  /**
   * 构造 嵌套属性变量引用 表达式
   * 
   * @param first 变量
   * @param child 嵌套变量
   * @return 嵌套属性变量 表达式
   */
  protected abstract VariableExpressionInstance buildPropertyVariableExpressionInstance(String name);

  private VariableExpressionInstance getCollectionVariable() throws RuleClassException {
    if (pos >= max) {
      throw RuleClassException.syntaxException(pos, "变量取值语法错误");
    }
    String name = tokens.get(pos).getValue();
    VariableExpressionInstance first = buildCollectionVariableExpressionInstance(name);

    pos += 2;
    VariableExpressionInstance child = getChildVariable();
    if (null != child) {
      return buildNestVariableExpressionInstance(first, child);
    }
    return first;
  }

  /**
   * 构造 嵌套索引变量引用 表达式
   * 
   * @param first 变量
   * @param child 嵌套变量
   * @return 嵌套索引变量 表达式
   */
  protected abstract VariableExpressionInstance buildCollectionVariableExpressionInstance(String name);

  private TokenKind next() {
    if (pos + 1 < max) {
      return tokens.get(pos + 1).getKind();
    }
    return null;
  }
}
