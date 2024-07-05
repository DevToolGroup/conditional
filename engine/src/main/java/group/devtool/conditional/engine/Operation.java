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

public class Operation {

  public enum Arith {

    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    POWER("^"),
    MOD("%"),
    ;

    private String op;

    Arith(String op) {
      this.op = op;
    }

    public static Arith get(String op) {
      Arith[] values = Arith.values();
      for (Arith value : values) {
        if (value.op.equals(op)) {
          return value;
        }
      }
      return null;
    }

    public String op() {
      return op;
    }
  }
  
  public enum Compare {

    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    EQ("=="),
    NE("!=");

    private String op;

    Compare(String op) {
      this.op = op;
    }

    public static Compare get(String op) {
      Compare[] values = Compare.values();
      for (Compare value : values) {
        if (value.op.equals(op)) {
          return value;
        }
      }
      return null;
    }

    public String op() {
      return op;
    }
  }

  public enum Logic {

    AND("&&"),
    OR("||"),
    NOT("!");

    private String op;

    Logic(String op) {
      this.op = op;
    }

    public static Logic get(String op) {
      Logic[] values = Logic.values();
      for (Logic logic : values) {
        if (logic.op.equals(op)) {
          return logic;
        }
      }
      return null;
    }

    public String op() {
      return op;
    }

  }

}
