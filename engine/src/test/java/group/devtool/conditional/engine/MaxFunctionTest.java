package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MaxFunctionTest {

    private MaxFunction maxFunction;

    @Before
    public void setUp() {
        maxFunction = new MaxFunction();
    }

    @Test
    public void apply_WithValidDoubleArguments_ReturnsMax() throws RuleInstanceException.RuleInstanceFunctionException {
        Number result = maxFunction.apply(1.0, 2.0);
        assertEquals(2.0, result);
    }

    @Test
    public void apply_WithValidFloatArguments_ReturnsMax() throws RuleInstanceException.RuleInstanceFunctionException {
        Number result = maxFunction.apply(1.0f, 2.0f);
        assertEquals(2.0f, result);
    }

    @Test
    public void apply_WithValidBigDecimalArguments_ReturnsMax() throws RuleInstanceException.RuleInstanceFunctionException {
        Number result = maxFunction.apply(new BigDecimal("1.0"), new BigDecimal("2.0"));
        assertEquals(new BigDecimal("2.0"), result);
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithNullArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        maxFunction.apply(null, null);
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithIncorrectNumberOfArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        maxFunction.apply(1.0);
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithNonNumberArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        maxFunction.apply("1.0", "2.0");
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithMixedTypeArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        maxFunction.apply(1.0, new BigDecimal("2.0"));
    }
}
