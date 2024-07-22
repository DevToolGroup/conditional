package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class CharAtFunctionTest {

    private CharAtFunction charAtFunction;

    @Before
    public void setUp() {
        charAtFunction = new CharAtFunction();
    }

    @Test
    public void apply_ValidStringAndIndex_ReturnsChar() throws RuleInstanceFunctionException {
        Object[] args = {"hello", 1};
        Character result = charAtFunction.apply(args);
        assertEquals(Character.valueOf('e'), result);
    }

    @Test
    public void apply_NullArgs_ThrowsException() {
        try {
            charAtFunction.apply(null);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceFunctionException e) {
            assertEquals("AT函数需要一个字符串参数，一个位置参数", e.getMessage());
        }
    }

    @Test
    public void apply_InvalidArgTypes_ThrowsException() {
        Object[] args = {"hello", "1"};
        try {
            charAtFunction.apply(args);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceFunctionException e) {
            assertEquals("AT函数需要一个字符串，列表，字典参数", e.getMessage());
        }
    }

    @Test
    public void apply_InvalidArgLength_ThrowsException() {
        Object[] args = {"hello"};
        try {
            charAtFunction.apply(args);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceFunctionException e) {
            assertEquals("AT函数需要一个字符串参数，一个位置参数", e.getMessage());
        }
    }

    @Test
    public void apply_NegativeIndex_ThrowsException() {
        Object[] args = {"hello", -1};
        try {
            charAtFunction.apply(args);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("String index out of range: -1", e.getMessage());
        } catch (RuleInstanceFunctionException e) {
            fail("Unexpected RuleInstanceFunctionException to be thrown");
        }
    }

    @Test
    public void apply_IndexGreaterThanStringLength_ThrowsException() {
        Object[] args = {"hello", 10};
        try {
            charAtFunction.apply(args);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("String index out of range: 10", e.getMessage());
        } catch (RuleInstanceFunctionException e) {
            fail("Unexpected RuleInstanceFunctionException to be thrown");
        }
    }
}
