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

/**
 * {@link FactPropertyClass} 默认实现
 */
public class FactPropertyClassImpl implements FactPropertyClass {

  private String type;

  private String code;

  private String name;

  private String valueType;

  private String keyType;

  public FactPropertyClassImpl() {
  }

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

  public void setType(String type) {
    this.type = type;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }
}
