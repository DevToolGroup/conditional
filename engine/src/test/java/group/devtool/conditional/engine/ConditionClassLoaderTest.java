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
import group.devtool.conditional.engine.ReteNode.AlphaNode;
import group.devtool.conditional.engine.ReteNode.RootNode;
import group.devtool.conditional.engine.ReteNode.ValueNode;

public class ConditionClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success = "";

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
  }

  @Test
  public void load() {
    ConditionClassLoader loader = new ConditionClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      loader.load(0, cs, ruleClass);

      ConditionClassGroup group = ruleClass.getConditionGroup();
      assertEquals(1, group.getConditions().size());
      assertTrue(group instanceof ReteConditionClassGroup);

      ReteNode node = ((ReteConditionClassGroup) group).getRoot();
      List<ReteNode> child = node.getChild();
      assertEquals(7, child.size());

      RootNode root = (RootNode) node;

      ValueNode userId = new ValueNode("user.id");
      ReteNode inUserId = root.get(userId);
      assertNotNull(inUserId);

      ValueNode orderUserId = new ValueNode("order.userId");
      ReteNode inOrderUserId = root.get(orderUserId);
      assertNotNull(inOrderUserId);

      ValueNode orderAmount = new ValueNode("order.amount");
      ReteNode inOrderAmount = root.get(orderAmount);
      assertNotNull(inOrderAmount);

      ValueNode dayScore = new ValueNode("dayScore");
      ReteNode inDayScore = root.get(dayScore);
      assertNotNull(inDayScore);

      ValueNode V100 = new ValueNode("100");
      ReteNode inV100 = root.get(V100);
      assertNotNull(inV100);

      ValueNode V500 = new ValueNode("500");
      ReteNode inV500 = root.get(V500);
      assertNotNull(inV500);

      ValueNode V10000 = new ValueNode("10000");
      ReteNode inV10000 = root.get(V10000);
      assertNotNull(inV10000);

      AlphaNode userAlphaNode = new AlphaNode("user.id==order.userId");
      assertTrue(1 == inUserId.getChild().size());
      assertTrue(inUserId.getChild().get(0).getKey().equals(userAlphaNode.getKey()));

      assertTrue(1 == inOrderUserId.getChild().size());
      assertTrue(inOrderUserId.getChild().get(0).getKey().equals(userAlphaNode.getKey()));

      AlphaNode amountAlphaNode1 = new AlphaNode("order.amount>100");
      AlphaNode amountAlphaNode2 = new AlphaNode("order.amount<500");
      assertTrue(2 == inOrderAmount.getChild().size());
      assertTrue(
          inOrderAmount.getChild().stream().map(i -> i.getKey()).anyMatch(i -> i.equals(amountAlphaNode1.getKey())));
      assertTrue(
          inOrderAmount.getChild().stream().map(i -> i.getKey()).anyMatch(i -> i.equals(amountAlphaNode2.getKey())));

      AlphaNode dayScoreAlphaNode = new AlphaNode("dayScore<10000");
      assertTrue(1 == inDayScore.getChild().size());
      assertEquals(dayScoreAlphaNode.getKey(), inDayScore.getChild().get(0).getKey());

      AlphaNode andAlphaNode = new AlphaNode(
          "user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000");
      assertEquals(andAlphaNode.getKey(), inUserId.getChild().get(0).getChild().get(0).getKey());

      AlphaNode inOrderAmountChild = (AlphaNode) inOrderAmount.getChild().stream()
          .filter(i -> i.getKey().equals(amountAlphaNode1.getKey())).findFirst().get();
      assertEquals("order.amount>100&&order.amount<500&&dayScore<10000", inOrderAmountChild.getChild().get(0).getKey());

      AlphaNode andAlphaNode2 = new AlphaNode("order.amount<500&&dayScore<10000");
      AlphaNode inOrderAmountChild2 = (AlphaNode) inOrderAmount.getChild().stream()
          .filter(i -> i.getKey().equals(amountAlphaNode2.getKey())).findFirst().get();
      assertEquals(andAlphaNode2.getKey(), inOrderAmountChild2.getChild().get(0).getKey());

      AlphaNode inOrderAmountChild3 = (AlphaNode) inOrderAmount.getChild().stream()
          .filter(i -> i.getKey().equals(amountAlphaNode2.getKey())).findFirst().get();
      assertEquals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000",
          inOrderAmountChild3.getChild().get(0).getChild().get(0).getChild().get(0).getKey());


      ConditionClass conditionClass = ruleClass.getConditionGroup().getConditions().get(0);
      List<ExpressionClass> functions = conditionClass.getFunctions();
      assertEquals(1, functions.size());

      ExpressionClass func = functions.get(0);
      assertTrue(func.getInstance() instanceof FunctionExpressionInstance);
      FunctionExpressionInstance fi = (FunctionExpressionInstance)func.getInstance();
      assertEquals("SET", fi.getFunctionName());
      assertEquals("score.score", fi.getArguments().get(0).getExpressionString());
      assertEquals("100", fi.getArguments().get(1).getExpressionString());

    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

}
