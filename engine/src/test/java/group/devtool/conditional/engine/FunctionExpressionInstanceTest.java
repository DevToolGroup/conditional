package group.devtool.conditional.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionExpressionInstanceTest {

	public List<Character> ns1 = new ArrayList<>();

	public List<Character> ns2 = new ArrayList<>();

	public List<Character> ns3 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "SUBS(\"function\", 0, 4)\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
		}

		String negative2 = "SUBS(var, 0, 4)\n";
		for (int i = 0; i < negative2.length(); i++) {
			ns2.add(negative2.charAt(i));
		}

		String negative3 = "SUBS(var, 0, v2 * 2)\n";
		for (int i = 0; i < negative3.length(); i++) {
			ns3.add(negative3.charAt(i));
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
			Assert.assertTrue(instance instanceof FunctionExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals("func", value);
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
			Assert.assertTrue(instance instanceof FunctionExpressionInstance);

			Map<String, Object> params = new HashMap<>();
			params.put("var", "function");
			Object value = instance.getCacheObject(new TestRuleInstance(params));

			Assert.assertEquals("func", value);
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
			Assert.assertTrue(instance instanceof FunctionExpressionInstance);

			Map<String, Object> params = new HashMap<>();
			params.put("var", "function");
			params.put("v2", 2);
			Object value = instance.getCacheObject(new TestRuleInstance(params));

			Assert.assertEquals("func", value);
		} catch (RuleClassException | RuleInstanceException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}


}
