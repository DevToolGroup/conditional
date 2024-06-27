package group.devtool.conditional.engine;

import java.util.Collection;

public class CollectionVariableExpressionInstanceImpl implements VariableExpressionInstance {

  private Integer name;

  public CollectionVariableExpressionInstanceImpl(String name) {
    this.name = Integer.parseInt(name);
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object value = context.peek();
    if (value instanceof Collection) {
      return ((Collection<?>) value).toArray()[name];
    } else {
      throw RuleInstanceException.unexpectedException("预期数据类型为集合类型，实际类型：" + value.getClass().getSimpleName());
    }
  }

  @Override
  public String getExpressionString() {
    return "[+" + name + "+]";
  }

  @Override
  public String getName() {
    return String.valueOf(name);
  }

}
