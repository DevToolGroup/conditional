package group.devtool.conditional.engine;

public class PropertyVariableExpressionInstanceImpl extends VariableExpressionInstanceImpl {

  private String name;

  public PropertyVariableExpressionInstanceImpl(String name) {
    super(name);
  }

  @Override
  public String getExpressionString() {
    return "." + name;
  }

}
