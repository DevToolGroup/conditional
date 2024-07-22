package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SubFunctionTest {

    private SubFunction subFunction;

    @Before
    public void setUp() {
        subFunction = new SubFunction();
    }

    @Test
    public void apply_ValidArguments_ReturnsSubstring() throws RuleInstanceFunctionException {
        Object[] args = {"123456789", 2, 5};
        String result = subFunction.apply(args);
        assertEquals("345", result);
    }

    @Test
    public void apply_NullArgs_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> subFunction.apply(null));
    }

    @Test
    public void apply_InvalidArgLength_ThrowsException() {
        Object[] args = {"123456789"};
        assertThrows(RuleInstanceFunctionException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_NonStringFirstArg_ThrowsException() {
        Object[] args = {123, 2, 5};
        assertThrows(ClassCastException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_NonIntegerSecondArg_ThrowsException() {
        Object[] args = {"123456789", "2", 5};
        assertThrows(ClassCastException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_NonIntegerThirdArg_ThrowsException() {
        Object[] args = {"123456789", 2, "5"};
        assertThrows(ClassCastException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_StartIndexGreaterThanEndIndex_ThrowsException() {
        Object[] args = {"123456789", 5, 2};
        assertThrows(IndexOutOfBoundsException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_StartIndexNegative_ThrowsException() {
        Object[] args = {"123456789", -1, 5};
        assertThrows(IndexOutOfBoundsException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_EndIndexNegative_ThrowsException() {
        Object[] args = {"123456789", 2, -1};
        assertThrows(IndexOutOfBoundsException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_StartIndexGreaterThanStringLength_ThrowsException() {
        Object[] args = {"123456789", 10, 15};
        assertThrows(IndexOutOfBoundsException.class, () -> subFunction.apply(args));
    }

    @Test
    public void apply_EndIndexGreaterThanStringLength_ThrowsException() {
        Object[] args = {"123456789", 2, 15};
        assertThrows(IndexOutOfBoundsException.class, () -> subFunction.apply(args));
    }
}
