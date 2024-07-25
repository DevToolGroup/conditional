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

import java.util.List;

public class FactClassImpl implements FactClass {

  private String code;

  private String name;

  private List<FactPropertyClass> properties;

  public FactClassImpl() {

  }

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

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProperties(List<FactPropertyClass> properties) {
    this.properties = properties;
  }
}
