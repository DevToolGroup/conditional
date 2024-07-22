package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LengthFunctionTest {

    private LengthFunction lengthFunction;

    @Before
    public void setUp() {
        lengthFunction = new LengthFunction();
    }

    @Test
    public void apply_StringInput_ReturnsLength() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Integer.valueOf(5), lengthFunction.apply("hello"));
    }

    @Test
    public void apply_ListInput_ReturnsSize() throws RuleInstanceException.RuleInstanceFunctionException {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals(Integer.valueOf(3), lengthFunction.apply(list));
    }

    @Test
    public void apply_MapInput_ReturnsSize() throws RuleInstanceException.RuleInstanceFunctionException {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        assertEquals(Integer.valueOf(3), lengthFunction.apply(map));
    }

    @Test
    public void apply_NullInput_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> lengthFunction.apply((Object[]) null));
    }

    @Test
    public void apply_WrongTypeInput_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> lengthFunction.apply(123));
    }

    @Test
    public void apply_MultipleArguments_ThrowsException() {
        assertThrows(RuleInstanceException.RuleInstanceFunctionException.class, () -> lengthFunction.apply("hello", "world"));
    }

    @Test
    public void apply_EmptyList_ReturnsZero() throws RuleInstanceException.RuleInstanceFunctionException {
        List<String> emptyList = new ArrayList<>();
        assertEquals(Integer.valueOf(0), lengthFunction.apply(emptyList));
    }

    @Test
    public void apply_EmptyMap_ReturnsZero() throws RuleInstanceException.RuleInstanceFunctionException {
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(Integer.valueOf(0), (Integer) lengthFunction.apply(emptyMap));
    }

    @Test
    public void apply_EmptyString_ReturnsZero() throws RuleInstanceException.RuleInstanceFunctionException {
        assertEquals(Integer.valueOf(0), lengthFunction.apply(""));
    }
}
