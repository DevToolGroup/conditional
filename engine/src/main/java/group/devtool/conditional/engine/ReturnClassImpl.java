package group.devtool.conditional.engine;

public class ReturnClassImpl implements ReturnClass {

  private String type;

  private String code;

  public ReturnClassImpl(String type, String code) {
    this.type = type;
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getType() {
    return type;
  }

}
