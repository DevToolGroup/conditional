package group.devtool.conditional.engine;

/**
 * {@link VariableExpressionInstance} 属性嵌套访问变量实例
 */
public class NestVariableExpressionInstanceImpl implements VariableExpressionInstance {

  private VariableExpressionInstance getter;

  private VariableExpressionInstance childGetter;

  public NestVariableExpressionInstanceImpl(VariableExpressionInstance getter, VariableExpressionInstance childGetter) {
    this.getter = getter;
    this.childGetter = childGetter;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object obj = getter.getObject(context);
    context.push(obj);
    Object value = childGetter.getObject(context);
    context.pop();
    return value;
  }

  @Override
  public String getExpressionString() {
    return getter.getExpressionString() + childGetter.getExpressionString();
  }

  @Override
  public String getName() {
    return getter.getName();
  }

}
