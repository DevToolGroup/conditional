package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ListFunctionTest {

    private ListFunction listFunction;

    @Before
    public void setUp() {
        listFunction = new ListFunction();
    }

    @Test
    public void apply_NoArguments_ThrowsException() {
        try {
            listFunction.apply();
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("LIST函数至少提供一个参数", e.getMessage());
        }
    }

    @Test
    public void apply_OneArgument_ReturnsListWithOneElement() throws RuleInstanceException.RuleInstanceFunctionException {
        Object arg = new Object();
        List<Object> result = listFunction.apply(arg);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(arg, result.get(0));
    }

    @Test
    public void apply_MultipleArguments_ReturnsListWithAllElements() throws RuleInstanceException.RuleInstanceFunctionException {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();
        List<Object> result = listFunction.apply(arg1, arg2, arg3);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(arg1, result.get(0));
        assertEquals(arg2, result.get(1));
        assertEquals(arg3, result.get(2));
    }

    @Test
    public void apply_NullArgument_ReturnsListWithNullElement() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Object> result = listFunction.apply((Object) null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void apply_EmptyArguments_ThrowsException() {
        try {
            listFunction.apply(new Object[0]);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("LIST函数至少提供一个参数", e.getMessage());
        }
    }
}
