package group.devtool.conditional.engine;


import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AbsFunctionTest {

	private AbsFunction absFunction;

	@Before
	public void setUp() {
		absFunction = new AbsFunction();
	}

	@Test
	public void apply_WithValidDouble_ReturnsAbsoluteValue() throws RuleInstanceException.RuleInstanceFunctionException {
		assertEquals(Double.valueOf(5.0), absFunction.apply(Double.valueOf(-5.0)));
	}

	@Test
	public void apply_WithValidFloat_ReturnsAbsoluteValue() throws RuleInstanceException.RuleInstanceFunctionException {
		assertEquals(Float.valueOf(5.0f), absFunction.apply(Float.valueOf(-5.0f)));
	}

	@Test
	public void apply_WithValidInteger_ReturnsAbsoluteValue() throws RuleInstanceException.RuleInstanceFunctionException {
		assertEquals(Integer.valueOf(5), absFunction.apply(Integer.valueOf(-5)));
	}

	@Test
	public void apply_WithValidLong_ReturnsAbsoluteValue() throws RuleInstanceException.RuleInstanceFunctionException {
		assertEquals(Long.valueOf(5L), absFunction.apply(Long.valueOf(-5L)));
	}

	@Test
	public void apply_WithValidBigDecimal_ReturnsAbsoluteValue() throws RuleInstanceException.RuleInstanceFunctionException {
		assertEquals(BigDecimal.valueOf(5.0), absFunction.apply(BigDecimal.valueOf(-5.0)));
	}

	@Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
	public void apply_WithNullArgs_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
		absFunction.apply(null);
	}

	@Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
	public void apply_WithMultipleArgs_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
		absFunction.apply(1, 2);
	}

	@Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
	public void apply_WithNonNumberArg_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
		absFunction.apply("not a number");
	}

	@Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
	public void apply_WithEmptyArgs_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
		absFunction.apply();
	}
}

