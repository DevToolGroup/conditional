package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionLoader;
import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionToken;

public class ExpressionClassTest {

  public List<Character> andCS = new ArrayList<>();

  public String andSuccess;

  public List<Character> orCS = new ArrayList<>();

  public String orSuccess;

  public List<Character> chCS = new ArrayList<>();

  public String chSuccess;

  public List<Character> arCS = new ArrayList<>();

  public String arSuccess;

  public List<Character> fnCS = new ArrayList<>();

  public String fnSuccess;

  public List<Character> efnCS = new ArrayList<>();

  public String efnSuccess;

  public List<Character> lfnCS = new ArrayList<>();

  public String lfnSuccess;

  public List<Character> afnCS = new ArrayList<>();

  public String afnSuccess;

  @Before
  public void read() {
    andSuccess = "user.id != 1 && user.id == order.userId && order.amount > 100 && order.amount < 500\n";
    for (int i = 0; i < andSuccess.length(); i++) {
      andCS.add(andSuccess.charAt(i));
    }

    orSuccess = "user.id != 1 && user.id == order.userId || order.amount > 100 && order.amount < 500\n";
    for (int i = 0; i < orSuccess.length(); i++) {
      orCS.add(orSuccess.charAt(i));
    }

    chSuccess = "user.id != 1 && (user.id == order.userId || order.amount > 100) && order.amount < 500\n";
    for (int i = 0; i < chSuccess.length(); i++) {
      chCS.add(chSuccess.charAt(i));
    }

    arSuccess = "user.score * 2 > 200 / 1";
    for (int i = 0; i < arSuccess.length(); i++) {
      arCS.add(arSuccess.charAt(i));
    }

    fnSuccess = "SET(user.score * 2, 2) > 200 / 1";
    for (int i = 0; i < fnSuccess.length(); i++) {
      fnCS.add(fnSuccess.charAt(i));
    }

    efnSuccess = "SET() > 200 / 1";
    for (int i = 0; i < efnSuccess.length(); i++) {
      efnCS.add(efnSuccess.charAt(i));
    }

    lfnSuccess = "SET(user.id != 1 && user.id == order.userId && order.amount > 100 && order.amount < 500) > 200 / 1";
    for (int i = 0; i < lfnSuccess.length(); i++) {
      lfnCS.add(lfnSuccess.charAt(i));
    }

    afnSuccess = "SET(user.id != 1 && (user.id == order.userId || order.amount > 100) && order.amount < 500, SET(3*2)) > SET(200 / 1) * 2";
    for (int i = 0; i < afnSuccess.length(); i++) {
      afnCS.add(afnSuccess.charAt(i));
    }

  }

  @Test
  public void testAndExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, andCS, null);
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof LogicExpressionInstance);

    LogicExpressionInstance logic = (LogicExpressionInstance) instance;
    ExpressionInstance left = logic.left();
    ExpressionInstance right = logic.right();

    assertEquals("user.id!=1", left.getExpressionString());
    assertEquals("user.id==order.userId&&order.amount>100&&order.amount<500", right.getExpressionString());
  }

  @Test
  public void testOrExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, orCS, null);
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof LogicExpressionInstance);

    LogicExpressionInstance logic = (LogicExpressionInstance) instance;
    ExpressionInstance left = logic.left();
    ExpressionInstance right = logic.right();

    assertEquals("user.id!=1&&user.id==order.userId", left.getExpressionString());
    assertEquals("order.amount>100&&order.amount<500", right.getExpressionString());
  }

  @Test
  public void testChildExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, chCS, null);
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof LogicExpressionInstance);

    LogicExpressionInstance logic = (LogicExpressionInstance) instance;
    ExpressionInstance left = logic.left();
    ExpressionInstance right = logic.right();

    assertEquals("user.id!=1", left.getExpressionString());
    assertEquals("(user.id==order.userId||order.amount>100)&&order.amount<500", right.getExpressionString());

    assertTrue(right instanceof LogicExpressionInstance);
    LogicExpressionInstance logicRight = (LogicExpressionInstance) right;

    assertEquals("(user.id==order.userId||order.amount>100)", logicRight.left().getExpressionString());
    assertEquals("order.amount<500", logicRight.right().getExpressionString());

  }

  @Test
  public void testArithExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, arCS, null);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof CompareExpressionInstance);

    CompareExpressionInstance compare = (CompareExpressionInstance) instance;
    ExpressionInstance left = compare.left();
    ExpressionInstance right = compare.right();

    assertEquals("user.score*2", left.getExpressionString());
    assertEquals(">", compare.getCompare().op());
    assertEquals("200/1", right.getExpressionString());

  }

  @Test
  public void testFuncExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, fnCS, null);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof CompareExpressionInstance);

    CompareExpressionInstance compare = (CompareExpressionInstance) instance;
    ExpressionInstance left = compare.left();
    ExpressionInstance right = compare.right();

    assertEquals("SET(user.score*2,2)", left.getExpressionString());
    assertEquals(">", compare.getCompare().op());
    assertEquals("200/1", right.getExpressionString());

  }

  @Test
  public void testEmptyFuncExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, efnCS, null);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof CompareExpressionInstance);

    CompareExpressionInstance compare = (CompareExpressionInstance) instance;
    ExpressionInstance left = compare.left();
    ExpressionInstance right = compare.right();

    assertEquals("SET()", left.getExpressionString());
    assertEquals(">", compare.getCompare().op());
    assertEquals("200/1", right.getExpressionString());

  }

  @Test
  public void testLogicFuncExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, lfnCS, null);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof CompareExpressionInstance);

    CompareExpressionInstance compare = (CompareExpressionInstance) instance;
    ExpressionInstance left = compare.left();
    ExpressionInstance right = compare.right();

    assertEquals("SET(user.id!=1&&user.id==order.userId&&order.amount>100&&order.amount<500)", left.getExpressionString());
    assertEquals(">", compare.getCompare().op());
    assertEquals("200/1", right.getExpressionString());

  }

  @Test
  public void testArithFuncExpressionClass() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, afnCS, null);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionToken token = loader.pop();

    VariableExpressionClass expression = null;
    try {
      expression = new VariableExpressionClass(token.getTokens());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ExpressionInstance instance = expression.getInstance();
    assertTrue(instance instanceof CompareExpressionInstance);

    CompareExpressionInstance compare = (CompareExpressionInstance) instance;
    ExpressionInstance left = compare.left();
    ExpressionInstance right = compare.right();

    assertEquals("SET(user.id!=1&&(user.id==order.userId||order.amount>100)&&order.amount<500,SET(3*2))", left.getExpressionString());
    assertEquals(">", compare.getCompare().op());
    assertEquals("SET(200/1)*2", right.getExpressionString());

  }

}
