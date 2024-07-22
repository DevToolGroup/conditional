package group.devtool.conditional.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionLoader;
import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArithExpressionInstanceImplTest {

	public List<Character> ns1 = new ArrayList<>();

	public List<Character> ns2 = new ArrayList<>();

	public List<Character> ns3 = new ArrayList<>();

	public List<Character> ns4 = new ArrayList<>();

	public List<Character> ns5 = new ArrayList<>();

	public List<Character> ns6 = new ArrayList<>();

	public List<Character> ns7 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "2 - -1\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
		}

		String negative2 = "-2 - -1\n";
		for (int i = 0; i < negative2.length(); i++) {
			ns2.add(negative2.charAt(i));
		}

		String negative3 = "-2 + -1\n";
		for (int i = 0; i < negative3.length(); i++) {
			ns3.add(negative3.charAt(i));
		}

		String negative4 = "-2 + 1\n";
		for (int i = 0; i < negative4.length(); i++) {
			ns4.add(negative4.charAt(i));
		}

		String negative5 = "2 + -1\n";
		for (int i = 0; i < negative5.length(); i++) {
			ns5.add(negative5.charAt(i));
		}

		String negative6 = "2 * -1\n";
		for (int i = 0; i < negative6.length(); i++) {
			ns6.add(negative6.charAt(i));
		}

		String negative7 = "2 / -2\n";
		for (int i = 0; i < negative7.length(); i++) {
			ns7.add(negative7.charAt(i));
		}


	}

	@Test
	public void getCacheObject1() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns1, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(3, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject2() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns2, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(-1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject3() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns3, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(-3, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject4() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns4, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(-1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject5() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns5, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject6() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns6, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(-2, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getCacheObject7() {
		ExpressionLoader loader = new ExpressionLoader();
		try {
			loader.load(0, ns7, null);
			ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			Object value = expressionClass.getInstance().getCacheObject(new TestRuleInstance(new HashMap<>()));
			Assert.assertEquals(-1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

}