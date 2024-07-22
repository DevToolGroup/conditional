package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MinFunctionTest {

    private MinFunction minFunction;

    @Before
    public void setUp() {
        minFunction = new MinFunction();
    }

    @Test
    public void apply_WithValidDoubleArguments_ReturnsMinimum() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Double.valueOf(1.0), minFunction.apply(1.0, 2.0));
    }

    @Test
    public void apply_WithValidFloatArguments_ReturnsMinimum() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Float.valueOf(1.0f), minFunction.apply(1.0f, 2.0f));
    }

    @Test
    public void apply_WithValidBigDecimalArguments_ReturnsMinimum() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(BigDecimal.valueOf(1.0), minFunction.apply(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0)));
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithNullArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        minFunction.apply(null, null);
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithIncorrectNumberOfArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        minFunction.apply(1.0);
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithNonNumberArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        minFunction.apply("string", 1.0);
    }

    @Test
    public void apply_WithEqualDoubleArguments_ReturnsEither() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Double.valueOf(1.0), minFunction.apply(1.0, 1.0));
    }

    @Test
    public void apply_WithEqualFloatArguments_ReturnsEither() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Float.valueOf(1.0f), minFunction.apply(1.0f, 1.0f));
    }

    @Test
    public void apply_WithEqualBigDecimalArguments_ReturnsEither() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(BigDecimal.valueOf(1.0), minFunction.apply(BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0)));
    }

    @Test(expected = RuleInstanceException.RuleInstanceFunctionException.class)
    public void apply_WithOneValidAndOneNullArgument_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        minFunction.apply(1.0, null);
    }
}
