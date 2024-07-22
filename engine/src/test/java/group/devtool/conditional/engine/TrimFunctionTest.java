package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TrimFunctionTest {

    private TrimFunction trimFunction;

    @Before
    public void setUp() {
        // Any setup required before each test can be placed here
        trimFunction = new TrimFunction();
    }

    @Test
    public void apply_NullArgs_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> trimFunction.apply(null));
    }

    @Test
    public void apply_EmptyArgs_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> trimFunction.apply());
    }

    @Test
    public void apply_InvalidTypeArgs_ThrowsException() {
        assertThrows(ClassCastException.class, () -> trimFunction.apply(123));
    }

    @Test
    public void apply_ValidString_ReturnsTrimmedString() throws RuleInstanceFunctionException {
        String input = "  test string  ";
        String expected = "test string";
        assertEquals(expected, trimFunction.apply(input));
    }

    @Test
    public void apply_StringWithNoSpaces_ReturnsSameString() throws RuleInstanceFunctionException {
        String input = "teststring";
        String expected = "teststring";
        assertEquals(expected, trimFunction.apply(input));
    }

}
