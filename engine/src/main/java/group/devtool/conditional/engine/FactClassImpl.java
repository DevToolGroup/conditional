package group.devtool.conditional.engine;

import java.util.List;

public class FactClassImpl implements FactClass {

  private String code;

  private String name;

  private List<FactPropertyClass> properties;

  public FactClassImpl(String code, String name, List<FactPropertyClass> properties) {
    this.code = code;
    this.name = name;
    this.properties = properties;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<FactPropertyClass> getProperties() {
    return properties;
  }

}
