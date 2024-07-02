package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.List;

public class VariableExpressionClass extends GenericExpressionClass {

  private List<VariableReference> references;

  public VariableExpressionClass(List<Token> tokens) throws RuleClassException {
    super(tokens);
  }

  @Override
  public List<VariableReference> getVariableReference() {
    if (null == references) {
      references = new ArrayList<>();
    }
    return references;
  }

}
