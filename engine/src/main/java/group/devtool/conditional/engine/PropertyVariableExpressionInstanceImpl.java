package group.devtool.conditional.engine;

public class PropertyVariableExpressionInstanceImpl extends VariableExpressionInstanceImpl {

  public PropertyVariableExpressionInstanceImpl(String name) {
    super(name);
  }

  @Override
  public String getExpressionString() {
    return "." + getName();
  }

}
