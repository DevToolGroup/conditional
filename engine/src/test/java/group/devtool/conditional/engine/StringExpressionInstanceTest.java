package group.devtool.conditional.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StringExpressionInstanceTest {

	public List<Character> ns1 = new ArrayList<>();

	@Before
	public void beforeArith() {
		String negative1 = "\"123\"\n";
		for (int i = 0; i < negative1.length(); i++) {
			ns1.add(negative1.charAt(i));
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
			Assert.assertTrue(instance instanceof StringExpressionInstance);

			Object value = instance.getCacheObject(new TestRuleInstance(new HashMap<>()));

			Assert.assertEquals("123", value);
		} catch (RuleClassException | RuleInstanceException e) {
			Assert.fail(e.getMessage());
		}
	}
}