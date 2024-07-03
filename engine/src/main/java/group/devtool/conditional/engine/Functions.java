package group.devtool.conditional.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统默认函数
 */
public enum Functions {
  PUT(new PutFunction()),
  ADD(new AddFunction()),
  LIST(new ListFunction()),
  Map(new MapFunction()),
  MAX(new MaxFunction()),
  SUBS(new SubFunction()),
  ABS(new AbsFunction()),
  AT(new CharAtFunction()),
  IN(new ContainsFunction()),
  LEN(new LengthFunction()),
  LOWER(new LowerFunction()),
  MIN(new MinFunction()),
  ROUND(new RoundFunction()),
  TRIMS(new TrimFunction()),
  FILTER(new FilterFunction()),
  SUM(new SumFunction()),
  RETRIEVE(new RetrieveFunction()),
  NOW(new NowFunction()),
  UPPER(new UpperFunction()),
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
