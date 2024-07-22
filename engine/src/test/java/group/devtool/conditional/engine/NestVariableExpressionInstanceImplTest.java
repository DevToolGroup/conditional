package group.devtool.conditional.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class NestVariableExpressionInstanceImplTest {

	public List<Character> ns1 = new ArrayList<>();

	public List<Character> ns2 = new ArrayList<>();

	public List<Character> ns3 = new ArrayList<>();

	public List<Character> ns4 = new ArrayList<>();

	public List<Character> ns5 = new ArrayList<>();

	public List<Character> ns6 = new ArrayList<>();

	public List<Character> ns7 = new ArrayList<>();

	public List<Character> ns8 = new ArrayList<>();

	public List<Character> ns9 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "a\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
		}

		String negative2 = "a.b\n";
		for (int i = 0; i < negative2.length(); i++) {
			ns2.add(negative2.charAt(i));
		}

		String negative3 = "a[0]\n";
		for (int i = 0; i < negative3.length(); i++) {
			ns3.add(negative3.charAt(i));
		}

		String negative4 = "a[0].b\n";
		for (int i = 0; i < negative4.length(); i++) {
			ns4.add(negative4.charAt(i));
		}

		String negative5 = "a.b[0]\n";
		for (int i = 0; i < negative5.length(); i++) {
			ns5.add(negative5.charAt(i));
		}

		String negative6 = "a.b[0][0]\n";
		for (int i = 0; i < negative6.length(); i++) {
			ns6.add(negative6.charAt(i));
		}

		String negative7 = "a[0][0][0]\n";
		for (int i = 0; i < negative7.length(); i++) {
			ns7.add(negative7.charAt(i));
		}

		String negative8 = "a.b.c.d\n";
		for (int i = 0; i < negative8.length(); i++) {
			ns8.add(negative8.charAt(i));
		}

		String negative9 = "a[0].b[0]\n";
		for (int i = 0; i < negative9.length(); i++) {
			ns9.add(negative9.charAt(i));
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
			Assert.assertTrue(instance instanceof VariableExpressionInstance);

			Map<String, Object> p = new HashMap<>();
			p.put("a", 1);
			Object value = instance.getCacheObject(new TestRuleInstance(p));

			Assert.assertEquals(1, value);
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
			Assert.assertTrue(instance instanceof NestVariableExpressionInstanceImpl);

			Map<String, Object> p = new HashMap<>();
			Map<String, Object> v = new HashMap<>();
			v.put("b", 1);
			p.put("a", v);
			Object value = instance.getCacheObject(new TestRuleInstance(p));

			Assert.assertEquals(1, value);
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
			Assert.assertTrue(instance instanceof NestVariableExpressionInstanceImpl);

			Map<String, Object> p = new HashMap<>();
			List<Object> v = new ArrayList<>();
			v.add(1);
			p.put("a", v);
			Object value = instance.getCacheObject(new TestRuleInstance(p));

			Assert.assertEquals(1, value);
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

			Assert.assertTrue(instance instanceof NestVariableExpressionInstanceImpl);

			Map<String, Object> p = new HashMap<>();
			List<Map<String, Object>> v = new ArrayList<>();
			Map<String, Object> iv = new HashMap<>();
			iv.put("b", 1);
			v.add(iv);
			p.put("a", v);
			Object value = instance.getCacheObject(new TestRuleInstance(p));

			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			e.printStackTrace();
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

			Map<String, Object> p = new HashMap<>();
			List<Object> v = new ArrayList<>();
			v.add(1);
			Map<String, List<Object>> iv = new HashMap<>();
			iv.put("b", v);
			p.put("a", iv);

			Object value = instance.getCacheObject(new TestRuleInstance(p));
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

			Map<String, Object> p = new HashMap<>();
			List<List<Object>> v = new ArrayList<>();
			v.add(Arrays.asList(1));
			Map<String, List<List<Object>>> iv = new HashMap<>();
			iv.put("b", v);
			p.put("a", iv);

			Object value = instance.getCacheObject(new TestRuleInstance(p));
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

			Map<String, Object> p = new HashMap<>();
			List<List<Object>> v = new ArrayList<>();
			v.add(Arrays.asList(1));
			List<List<List<Object>>> iv = new ArrayList<>();
			iv.add(v);
			p.put("a", iv);

			Object value = instance.getCacheObject(new TestRuleInstance(p));
			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getObject8() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns8, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();

			Map<String, Object> p = new HashMap<>();
			Map<String, Map<String, Map<String, Object>>> b = new HashMap<>();
			Map<String, Map<String, Object>> c = new HashMap<>();
			Map<String, Object> d = new HashMap<>();
			d.put("d", 1);
			c.put("c", d);
			b.put("b", c);
			p.put("a", b);

			Object value = instance.getCacheObject(new TestRuleInstance(p));
			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getObject9() {
		AbstractRuleClassLoader.ExpressionLoader loader = new AbstractRuleClassLoader.ExpressionLoader();
		try {
			loader.load(0, ns9, null);
			AbstractRuleClassLoader.ExpressionToken token = loader.pop();
			VariableExpressionClass expressionClass = new VariableExpressionClass(token.getTokens());
			ExpressionInstance instance = expressionClass.getInstance();

			Map<String, Object> p = new HashMap<>();

			List<Map<String, List<Object>>> b = new ArrayList<>();
			Map<String, List<Object>> c = new HashMap<>();
			List<Object> d = new ArrayList<>();
			d.add(1);
			c.put("b", d);
			b.add(c);
			p.put("a", b);

			Object value = instance.getCacheObject(new TestRuleInstance(p));
			Assert.assertEquals(1, value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}

}