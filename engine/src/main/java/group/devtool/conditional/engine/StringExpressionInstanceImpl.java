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
 * 字符串
 */
public class StringExpressionInstanceImpl implements StringExpressionInstance {

  private String value;

  public StringExpressionInstanceImpl(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      this.value = value.substring(1, value.length() - 1);
    } else {
      this.value = value;
    }
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    return value;
  }

  @Override
  public String getExpressionString() {
    return "\"" + value + "\"";
  }

}
