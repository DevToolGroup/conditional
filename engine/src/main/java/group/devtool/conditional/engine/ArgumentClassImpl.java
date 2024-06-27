package group.devtool.conditional.engine;

public class ArgumentClassImpl implements ArgumentClass {

  private String type;

  private String code;

  public ArgumentClassImpl(String type, String code) {
    this.type = type;
    this.code = code;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getCode() {
    return code;
  }

}
