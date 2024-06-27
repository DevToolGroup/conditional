package group.devtool.conditional.engine;

/**
 * 事实属性定义
 */
public interface FactPropertyClass {

  /**
   * @return 事实属性类型
   */
  public String getType();

  /**
   * @return 集合类型，Map类型，需要定义 ValueType
   */
  public String getValueType();

  /**
   * @return Map类型，需要定义 KeyType
   */
  public String getKeyType();

  /**
   * @return 事实属性编码
   */
  public String getCode();

  /**
   * @return 事实属性名称
   */
  public String getName();

}
