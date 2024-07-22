package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AddFunctionTest {

    private AddFunction addFunction;

    private List<Object> list;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        addFunction = new AddFunction();
    }

    @Test
    public void apply_ValidArguments_ListContainsAddedValue() throws RuleInstanceException.RuleInstanceFunctionException {
        addFunction.apply(list, 1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void apply_NullArguments_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> addFunction.apply(null));
    }

    @Test
    public void apply_IncorrectNumberOfArguments_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> addFunction.apply(list));
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> addFunction.apply(list, 1, 2));
    }

    @Test
    public void apply_TargetNotList_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> addFunction.apply(new Object(), 1));
    }

    @Test
    public void apply_ValidArgumentsWithDifferentTypes_ListContainsAddedValue() throws RuleInstanceException.RuleInstanceFunctionException {
        addFunction.apply(list, "test");
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test
    public void apply_EmptyList_AddsValue() throws RuleInstanceException.RuleInstanceFunctionException {
        addFunction.apply(list, "value");
        assertEquals(1, list.size());
        assertEquals("value", list.get(0));
    }
}
