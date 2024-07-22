package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RoundFunctionTest {

    private RoundFunction roundFunction;

    @Before
    public void setUp() {
        // Any setup code if required in future
        roundFunction = new RoundFunction();
    }

    @Test
    public void apply_WithDoubleParameter_ShouldReturnRoundedLong() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.456));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.45678));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.5));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.56789));
    }

    @Test
    public void apply_WithFloatParameter_ShouldReturnRoundedLong() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.456f));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.45678f));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.5f));
        assertEquals(Optional.of(10L).get(), roundFunction.apply(10.56789f));
    }

    @Test
    public void apply_WithBigDecimalParameter_ShouldReturnRoundedLong() throws RuleInstanceException.RuleInstanceFunctionException {
        BigDecimal decimal = new BigDecimal("10.456789");
        assertEquals(Optional.of(10L).get(), roundFunction.apply(decimal));
    }

    @Test
    public void apply_WithNullParameter_ShouldThrowException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> roundFunction.apply(null));
    }

    @Test
    public void apply_WithInvalidParameter_ShouldThrowException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> roundFunction.apply("invalid"));
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> roundFunction.apply(new Object()));
    }

    @Test
    public void apply_WithMultipleParameters_ShouldThrowException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> roundFunction.apply(10.5, 2));
    }
}
