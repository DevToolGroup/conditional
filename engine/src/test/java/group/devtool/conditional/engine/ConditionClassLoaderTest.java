package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.ConditionClassLoader;
import group.devtool.conditional.engine.Rete.ReteNode;
import group.devtool.conditional.engine.Rete.BetaNode;
import group.devtool.conditional.engine.Rete.RootNode;
import group.devtool.conditional.engine.Rete.TerminateNode;
import group.devtool.conditional.engine.Rete.AlphaNode;

public class ConditionClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success = "";

  public List<Character> chs = new ArrayList<>();

  public String chSuccess = "";

  @Before
  public void read() {
    success += "IF";
    success += "  user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000\n";
    success += "THEN\n";
    success += "  SET(score.score, 100)\n";
    success += "END";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }

    chSuccess += "IF";
    chSuccess += "  user.id == order.userId && (order.amount > 100 && order.amount < 500) && dayScore < 10000\n";
    chSuccess += "THEN\n";
    chSuccess += "  SET(score.score, 100)\n";
    chSuccess += "END";
    for (int i = 0; i < chSuccess.length(); i++) {
      chs.add(chSuccess.charAt(i));
    }
  }

  @Test
  public void loadSimple() {
    ConditionClassLoader loader = new ConditionClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      loader.load(0, cs, ruleClass);

      ConditionClassGroup group = ruleClass.getConditionGroup();
      assertEquals(1, group.getConditions().size());
      assertTrue(group instanceof ReteConditionClassGroup);

      Rete rete = ((ReteConditionClassGroup) group).getRete();
      ReteNode node = rete.getRoot();
      List<ReteNode> child = node.getChild();
      assertEquals(4, child.size());

      AlphaNode dayScore = new AlphaNode(rete, "dayScore<10000");
      ReteNode inDayScore = rete.get(dayScore);
      assertNotNull(inDayScore);

      AlphaNode orderAmount = new AlphaNode(rete, "order.amount>100");
      ReteNode inOrderAmount = rete.get(orderAmount);
      assertNotNull(inOrderAmount);
      ReteNode inOrderAmountChild = inOrderAmount.getChild().get(0);
      assertEquals("order.amount>100&&order.amount<500&&dayScore<10000", inOrderAmountChild.getKey());

      AlphaNode orderAmount2 = new AlphaNode(rete, "order.amount<500");
      ReteNode inOrderAmount2 = rete.get(orderAmount2);
      assertNotNull(inOrderAmount2);
      ReteNode inOrderAmountChild2 = inOrderAmount2.getChild().get(0);
      assertEquals("order.amount<500&&dayScore<10000", inOrderAmountChild2.getKey());

      AlphaNode userId = new AlphaNode(rete, "user.id==order.userId");
      ReteNode inUserId = rete.get(userId);
      assertNotNull(inUserId);
      ReteNode inUserIdChild = inUserId.getChild().get(0);
      assertEquals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000", inUserIdChild.getKey());
      assertTrue(inUserIdChild.getChild().get(0) instanceof TerminateNode);

    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void loadChild() {
    ConditionClassLoader loader = new ConditionClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      loader.load(0, chs, ruleClass);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    ConditionClassGroup group = ruleClass.getConditionGroup();
    assertEquals(1, group.getConditions().size());
    assertTrue(group instanceof ReteConditionClassGroup);
    Rete rete = ((ReteConditionClassGroup) group).getRete();
    ReteNode node = rete.getRoot();

    List<ReteNode> child = node.getChild();
    assertEquals(4, child.size());

    AlphaNode dayScore = new AlphaNode(rete, "dayScore<10000");
    ReteNode inDayScore = rete.get(dayScore);
    assertNotNull(inDayScore);
    ReteNode inDayScoreChild = inDayScore.getChild().get(0);
    assertEquals("(order.amount>100&&order.amount<500)&&dayScore<10000", inDayScoreChild.getKey());


    AlphaNode orderAmount = new AlphaNode(rete, "order.amount>100");
    ReteNode inOrderAmount = rete.get(orderAmount);
    assertNotNull(inOrderAmount);
    ReteNode inOrderAmountChild = inOrderAmount.getChild().get(0);
    assertEquals("order.amount>100&&order.amount<500", inOrderAmountChild.getKey());

    AlphaNode orderAmount2 = new AlphaNode(rete, "order.amount<500");
    ReteNode inOrderAmount2 = rete.get(orderAmount2);
    assertNotNull(inOrderAmount2);
    ReteNode inOrderAmountChild2 = inOrderAmount2.getChild().get(0);
    assertEquals("order.amount>100&&order.amount<500", inOrderAmountChild2.getKey());

    AlphaNode userId = new AlphaNode(rete, "user.id==order.userId");
    ReteNode inUserId = rete.get(userId);
    assertNotNull(inUserId);
    ReteNode inUserIdChild = inUserId.getChild().get(0);
    assertEquals("user.id==order.userId&&(order.amount>100&&order.amount<500)&&dayScore<10000", inUserIdChild.getKey());

    assertTrue(inUserIdChild.getChild().get(0) instanceof TerminateNode);

  }
}
