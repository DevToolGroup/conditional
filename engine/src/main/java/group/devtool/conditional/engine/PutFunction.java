package group.devtool.conditional.engine;

public class PutFunction implements ConditionFunction<Void> {

  @Override
  public String getName() {
    return "PUT";
  }

  @Override
  public Void apply(Object... args) {
    
    return null;
  }

}
