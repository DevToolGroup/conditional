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

public class RuleClassException extends Exception {

  public RuleClassException(String message) {
    super(message);
  }

  public static class RuleClassStreamException extends RuleClassException {

    public RuleClassStreamException(String message) {
      super(message);
    }
  
  }

  public static class RuleClassSyntaxException extends RuleClassException {

    private Integer pos;

    public RuleClassSyntaxException(int pos, CharSequence pattern) {
      super("语法错误。" + "位置：" + pos + " 字符" + pattern);
      this.pos = pos;
    }

    public RuleClassSyntaxException(String message) {
      super(message);
    }

    public Integer getPos() {
      return pos;
    }
  }

  public static RuleClassException streamException(String message) {
    return new RuleClassStreamException(message);
  }

  public static RuleClassException syntaxException(String message) {
    return new RuleClassSyntaxException(message);
  }

  public static RuleClassException syntaxException(int pos, CharSequence pattern) {
    return new RuleClassSyntaxException(pos, pattern);
  }

}
