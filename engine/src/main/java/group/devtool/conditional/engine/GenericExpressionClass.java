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

import java.util.List;

import group.devtool.conditional.engine.Operation.Arith;
import group.devtool.conditional.engine.Operation.Compare;
import group.devtool.conditional.engine.Operation.Logic;
import group.devtool.conditional.engine.VariableReference.NestVariableReference;
import group.devtool.conditional.engine.VariableReference.SimpleVariableReference;

public abstract class GenericExpressionClass extends AbstractExpressionClass {

  public GenericExpressionClass() {
    super();
  }

  public GenericExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
  }

  @Override
  protected ExpressionInstance buildLogicExpressionInstance(ExpressionInstance left, ExpressionInstance right,
      Logic logic) {
    return new LogicExpressionInstanceImpl(left, right, logic);
  }

  @Override
  protected ExpressionInstance buildCompareExpressionInstance(ExpressionInstance left, Compare operation,
      ExpressionInstance right) {
    return new CompareExpressionInstanceImpl(left, operation, right);
  }

  @Override
  protected ExpressionInstance buildArithExpression(ExpressionInstance left, Arith arith,
      ExpressionInstance right) {
    return new ArithExpressionInstanceImpl(left, arith, right);
  }

  @Override
  protected ExpressionInstance buildChildExpressionInstance(ExpressionInstance child) {
    return new ChildExpressionInstanceImpl(child);
  }

  @Override
  protected ExpressionInstance buildStringExpressionInstance(String value) {
    return new StringExpressionInstanceImpl(value);
  }

  @Override
  protected ExpressionInstance buildNumberExpressionInstance(String value, boolean positive) throws RuleClassException {
    return new NumberExpressionInstanceImpl(value, positive);
  }

  @Override
  protected VariableExpressionInstance buildVariableExpressionInstance(String value, boolean hasNest) {
    if (!hasNest) {
      getVariableReferences().add(new SimpleVariableReference(value));
    }
    return new VariableExpressionInstanceImpl(value);
  }

  @Override
  protected VariableExpressionInstance buildNestVariableExpressionInstance(ExpressionInstance first,
      VariableExpressionInstance child) {
    getVariableReferences().add(new NestVariableReference(first, child));
    return new NestVariableExpressionInstanceImpl(first, child);
  }

  @Override
  protected VariableExpressionInstance buildCollectionVariableExpressionInstance(String name) {
    return new CollectionVariableExpressionInstanceImpl(name);
  }

  @Override
  protected VariableExpressionInstance buildPropertyVariableExpressionInstance(String name) {
    return new PropertyVariableExpressionInstanceImpl(name);
  }

  @Override
  protected FunctionExpressionInstance buildFunctionExpressionInstance(String funcName,
      List<ExpressionInstance> arguments) {
    return new FunctionExpressionInstanceImpl(funcName, arguments);
  }

}
