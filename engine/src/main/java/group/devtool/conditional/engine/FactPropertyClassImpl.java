package group.devtool.conditional.engine;

/**
 * {@link FactPropertyClass} 默认实现
 */
public class FactPropertyClassImpl implements FactPropertyClass {

  private String type;

  private String code;

  private String name;

  private String valueType;

  private String keyType;

  public FactPropertyClassImpl(String type, String keyType, String valueType, String code, String name) {
    this.type = type;
    this.code = code;
    this.name = name;
    this.valueType = valueType;
  }

  public String getType() {
    return type;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  @Override
  public String getValueType() {
    return valueType;
  }

  @Override
  public String getKeyType() {
    return keyType;
  }

}
