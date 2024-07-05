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
