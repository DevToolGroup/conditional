package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.Rete.RootNode;
import group.devtool.conditional.engine.Rete.ReteNode;
import group.devtool.conditional.engine.Rete.AlphaNode;
import group.devtool.conditional.engine.Rete.TerminateNode;

public class RuleClassTest {

  public String dl = "";

  @Before
  public void read() {
    dl += "TYPE History \"积分历史\"\n";
    dl += "Time    publishTime \"时间\"\n";
    dl += "Integer count \"积分数量\"\n";
    dl += "END\n";

    dl += "TYPE User \"用户\"\n";
    dl += "Integer       id        \"用户ID\"\n";
    dl += "Integer       score     \"用户积分\"\n";
    dl += "List<History> histories \"积分历史\"\n";
    dl += "END\n";

    dl += "TYPE Order \"订单\"\n";
    dl += "User       user       \"用户\"\n";
    dl += "Integer    amount     \"订单金额\"\n";
    dl += "END\n";

    dl += "TYPE Score \"积分\"\n";
    dl += "Integer    score      \"积分数量\"\n";
    dl += "List<History>    histories     \"积分记录\"\n";
    dl += "END\n";

    dl += "ARG Order order, User user\n";

    dl += "RETURN Score score\n";

    dl += "CONST Integer dayScore \"当日积分数量\" = 0\n";
    dl += "CONST List<History> histories \"当日积分记录\" = Filter(user.histories, now())\n";

    dl += "IF\n";
    dl += "user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000\n";
    dl += "THEN\n";
    dl += "SET(score.score, 100)\n";
    dl += "END\n";

    dl += "IF\n";
    dl += "user.id == order.userId && order.amount > 500 && dayScore < 10000\n";
    dl += "THEN\n";
    dl += "SET(score.score, 500)\n";
    dl += "END\n";
  }

  @Test
  public void testRuleClass() {
    ReteRuleClassLoader loader = new ReteRuleClassLoader("id", dl);
    RuleClass ruleClass = null;
    try {
      ruleClass = loader.load();
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    FactClass historyFact = ruleClass.getFactClass("History");
    assertEquals("History", historyFact.getCode());
    assertEquals("积分历史", historyFact.getName());
    assertTrue(historyFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Time") && i.getCode().equals("publishTime") && i.getName().equals("时间")));
    assertTrue(historyFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Integer") && i.getCode().equals("count") && i.getName().equals("积分数量")));

    FactClass userFact = ruleClass.getFactClass("User");
    assertEquals("User", userFact.getCode());
    assertEquals("用户", userFact.getName());
    assertTrue(userFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Integer") && i.getCode().equals("id") && i.getName().equals("用户ID")));
    assertTrue(userFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Integer") && i.getCode().equals("score") && i.getName().equals("用户积分")));
    assertTrue(userFact.getProperties().stream().anyMatch(i -> i.getType().equals("List")
        && i.getCode().equals("histories") && i.getName().equals("积分历史") && i.getValueType().equals("History")));

    FactClass orderFact = ruleClass.getFactClass("Order");
    assertEquals("Order", orderFact.getCode());
    assertEquals("订单", orderFact.getName());
    assertTrue(orderFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("User") && i.getCode().equals("user") && i.getName().equals("用户")));
    assertTrue(orderFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Integer") && i.getCode().equals("amount") && i.getName().equals("订单金额")));

    FactClass scoreFact = ruleClass.getFactClass("Score");
    assertEquals("Score", scoreFact.getCode());
    assertEquals("积分", scoreFact.getName());
    assertTrue(scoreFact.getProperties().stream()
        .anyMatch(i -> i.getType().equals("Integer") && i.getCode().equals("score") && i.getName().equals("积分数量")));
    assertTrue(scoreFact.getProperties().stream().anyMatch(i -> i.getType().equals("List")
        && i.getCode().equals("histories") && i.getName().equals("积分记录") && i.getValueType().equals("History")));

    ArgumentClass orderArg = ruleClass.getArgumentClass("order");
    assertNotNull(orderArg);
    assertEquals("Order", orderArg.getType());

    ArgumentClass userArg = ruleClass.getArgumentClass("user");
    assertNotNull(userArg);
    assertEquals("User", userArg.getType());

    ReturnClass returnClass = ruleClass.getReturnClass();
    assertNotNull(returnClass);
    assertEquals("Score", returnClass.getType());

    VariableClass var1 = ruleClass.getVariableClass("dayScore");
    assertNotNull(var1);
    assertEquals("Integer", var1.getType());

    VariableClass var2 = ruleClass.getVariableClass("histories");
    assertNotNull(var2);
    assertEquals("List", var2.getType());
    assertEquals("History", var2.getValueType());

    ConditionClassGroup conditionClassGroup = ruleClass.getConditionGroup();
    assertNotNull(conditionClassGroup);
    assertTrue(conditionClassGroup instanceof ReteConditionClassGroup);
    ReteConditionClassGroup rcc = (ReteConditionClassGroup) conditionClassGroup;

    Rete rete = rcc.getRete();
    assertEquals(5, rete.getRoot().getChild().size());

    ReteNode v1Child = rete.get(new AlphaNode(rete, "user.id==order.userId"));
    assertNotNull(v1Child);

    assertEquals(2, v1Child.getChild().size());
    assertTrue(v1Child.getChild().stream()
        .anyMatch(i -> i.getKey().equals("user.id==order.userId&&order.amount>500&&dayScore<10000")));
    assertTrue(v1Child.getChild().stream()
        .anyMatch(i -> i.getKey().equals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000")));

    ReteNode v11 = v1Child.getChild().stream()
        .filter(i -> i.getKey().equals("user.id==order.userId&&order.amount>500&&dayScore<10000")).findFirst().get();
    assertTrue(v11.getChild().get(0) instanceof TerminateNode);
    TerminateNode t1 = (TerminateNode) v11.getChild().get(0);
    assertEquals("SET(score.score,500)", t1.getActions().get(0).getInstance().getExpressionString());

    ReteNode v12 = v1Child.getChild().stream()
        .filter(i -> i.getKey().equals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000"))
        .findFirst().get();
    assertTrue(v12.getChild().get(0) instanceof TerminateNode);
    TerminateNode t2 = (TerminateNode) v12.getChild().get(0);
    assertEquals("SET(score.score,100)", t2.getActions().get(0).getInstance().getExpressionString());

    ReteNode v2 = rete.getRoot().getChild().stream().filter(i -> i.getKey().equals("dayScore<10000")).findFirst().get();
    assertEquals(2, v2.getChild().size());
    assertTrue(v2.getChild().stream()
        .anyMatch(i -> i.getKey().equals("order.amount>500&&dayScore<10000")));
    assertTrue(v2.getChild().stream()
        .anyMatch(i -> i.getKey().equals("order.amount<500&&dayScore<10000")));

    ReteNode v3 = v2.getChild().stream().filter(i -> i.getKey().equals("order.amount<500&&dayScore<10000")).findFirst().get();
    assertEquals("order.amount>100&&order.amount<500&&dayScore<10000", v3.getChild().get(0).getKey());
  }

}
