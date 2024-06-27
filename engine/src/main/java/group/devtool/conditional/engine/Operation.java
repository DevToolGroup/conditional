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

  }

}
