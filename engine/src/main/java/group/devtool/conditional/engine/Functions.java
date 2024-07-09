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

import java.util.HashMap;
import java.util.Map;

/**
 * 系统默认函数
 */
public enum Functions {
  PUT(new PutFunction()),
  ADD(new AddFunction()),
  LIST(new ListFunction()),
  MAP(new MapFunction()),
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
