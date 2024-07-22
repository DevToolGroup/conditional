package group.devtool.conditional.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildExpressionInstanceTest {

	public List<Character> ns1 = new ArrayList<>();

	public List<Character> ns2 = new ArrayList<>();

	public List<Character> ns3 = new ArrayList<>();

	public List<Character> ns4 = new ArrayList<>();

	public List<Character> ns5 = new ArrayList<>();

	public List<Character> ns6 = new ArrayList<>();

	public List<Character> ns7 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "(\"123\")\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
		}

		String negative2 = "(123)\n";
		for (int i = 0; i < negative2.length(); i++) {
			ns2.add(negative2.charAt(i));
		}

		String negative3 = "(2 > 1)\n";
		for (int i = 0; i < negative3.length(); i++) {
			ns3.add(negative3.charAt(i));
		}

		String negative4 = "(2 > 1 && 3 < 2)\n";
		for (int i = 0; i < negative4.length(); i++) {
			ns4.add(negative4.charAt(i));
		}

		String negative5 = "( 3 - 2 )\n";
		for (int i = 0; i < negative5.length(); i++) {
			ns5.add(negative5.charAt(i));
		}

		String negative6 = "( (3 - 2) )\n";
		for (int i = 0; i < negative6.length(); i++) {
			ns6.add(negative6.charAt(i));
		}

		String negative7 = "( SUBS(\"function\", 0, 4) )\n";
		for (int i = 0; i < negative7.length(); i++) {
			ns7.add(negative7.charAt(i));
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
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals("123", value);
		} catch (RuleClassException | RuleInstanceException e) {
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
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals(123, value);
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
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertTrue((Boolean) value);
		} catch (RuleClassException | RuleInstanceException e) {
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
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertFalse((Boolean) value);
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
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getObject6() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns6, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getObject7() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns7, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();
			Assert.assertTrue(instance instanceof ChildExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals("func", value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}


}