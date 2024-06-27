package group.devtool.conditional.engine;

import java.util.HashMap;
import java.util.Map;

public enum Functions {
  PUT(new PutFunction()),
  ;

  private ConditionFunction<?> function;

  private Functions(ConditionFunction<?> function) {
    this.function = function;
  }

  public ConditionFunction<?> getFunction() {
    return function;
  }

  static Map<String, ConditionFunction<?>> toMap() {
    Map<String, ConditionFunction<?>> result = new HashMap<>();

    for (Functions functions : values()) {
      result.put(functions.getFunction().getName(), functions.getFunction());
    }
    return result;
  }

}
