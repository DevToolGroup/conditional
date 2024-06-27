package group.devtool.conditional.engine;

/**
 * 规则函数
 */
public interface ConditionFunction<T> {

  /**
   * @return 函数名
   */
  public String getName();

  /**
   * 执行函数
   * 
   * @param args 函数参数
   * @return 函数结果
   */
  public T apply(Object... args);

}
