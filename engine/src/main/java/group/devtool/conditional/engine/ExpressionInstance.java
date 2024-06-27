package group.devtool.conditional.engine;

/**
 * 表达式实例接口
 */
public interface ExpressionInstance {

  public String getExpressionString();

  /**
   * 根据上下文计算表达式结果
   * 
   * @param context 上下文
   * @return 计算结果
   * @throws RuleInstanceException 规则实例相关异常
   */
  public Object getObject(RuleInstance context) throws RuleInstanceException;

  default Object getCacheObject(RuleInstance context) throws RuleInstanceException {
    String key = this.getExpressionString();
    Object value = context.getExpressionValueOrDefault(key, getObject(context));
    context.cacheExpressionValue(key, value);
    return value;
  }

}
