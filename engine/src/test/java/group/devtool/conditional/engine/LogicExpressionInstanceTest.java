package group.devtool.conditional.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogicExpressionInstanceTest {

	public List<Character> ns1 = new ArrayList<>();

	public List<Character> ns2 = new ArrayList<>();

	public List<Character> ns3 = new ArrayList<>();

	public List<Character> ns4 = new ArrayList<>();

	public List<Character> ns5 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "1 == 1 && 1 > 1\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
		}

		String negative2 = "1 == 1 || 1 > 1\n";
		for (int i = 0; i < negative2.length(); i++) {
			ns2.add(negative2.charAt(i));
		}

		String negative3 = "! (1==1)";
		for (int i = 0; i < negative3.length(); i++) {
			ns3.add(negative3.charAt(i));
		}

		String negative4 = "! (1 < 1)";
		for (int i = 0; i < negative4.length(); i++) {
			ns4.add(negative4.charAt(i));
		}

		String negative5 = "1 == 1&& ! (1 < 1)";
		for (int i = 0; i < negative5.length(); i++) {
			ns5.add(negative5.charAt(i));
		}

	}

	@Test
	public void getObject1() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns1, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof LogicExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue(value instanceof Boolean);
			Assert.assertFalse((Boolean) value);
		} catch (RuleClassException | RuleInstanceException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getObject2() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns2, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof LogicExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue(value instanceof Boolean);
			Assert.assertTrue((Boolean) value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getObject3() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns3, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof LogicExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue(value instanceof Boolean);
			Assert.assertFalse((Boolean) value);

		} catch (RuleClassException | RuleInstanceException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getObject4() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns4, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof LogicExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue(value instanceof Boolean);
			Assert.assertTrue((Boolean) value);

		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getObject5() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns5, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof LogicExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue(value instanceof Boolean);
			Assert.assertTrue((Boolean) value);

		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}

	}
}