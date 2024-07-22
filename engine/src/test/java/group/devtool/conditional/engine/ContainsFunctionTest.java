package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import static org.junit.Assert.*;

public class ContainsFunctionTest {

    private ContainsFunction containsFunction;

    private List<String> list;
    private Map<String, String> map;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        containsFunction = new ContainsFunction();
    }

    @Test
    public void apply_StringContainsString_ReturnsTrue() throws RuleInstanceFunctionException {
        assertTrue(containsFunction.apply("abc", "b"));
    }

    @Test
    public void apply_StringContainsString_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply("abc", "d"));
    }

    @Test
    public void apply_ListContainsObject_ReturnsTrue() throws RuleInstanceFunctionException {
        assertTrue(containsFunction.apply(list, "b"));
    }

    @Test
    public void apply_ListContainsObject_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply(list, "d"));
    }

    @Test
    public void apply_MapContainsKey_ReturnsTrue() throws RuleInstanceFunctionException {
        assertTrue(containsFunction.apply(map, "b"));
    }

    @Test
    public void apply_MapContainsKey_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply(map, "d"));
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_InvalidNumberOfArguments_ThrowsException() throws RuleInstanceFunctionException {
        containsFunction.apply("abc");
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_NullArguments_ThrowsException() throws RuleInstanceFunctionException {
        containsFunction.apply(null, null);
    }

    @Test
    public void apply_NonStringNonListNonMap_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply(123, 456));
    }

    @Test
    public void apply_EmptyList_ReturnsFalse() throws RuleInstanceFunctionException {
        List<String> emptyList = new ArrayList<>();
        assertFalse(containsFunction.apply(emptyList, "a"));
    }

    @Test
    public void apply_NullElementInList_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply(list, null));
    }

    @Test
    public void apply_NullKeyInMap_ReturnsFalse() throws RuleInstanceFunctionException {
        assertFalse(containsFunction.apply(map, null));
    }
}
