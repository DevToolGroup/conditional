package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PutFunctionTest {

    private PutFunction putFunction;

    private Map<Object, Object> map;

    @Before
    public void setUp() {
        map = new HashMap<>();
        putFunction = new PutFunction();
    }

    @Test
    public void apply_ValidArguments_PutsValueInMap() throws RuleInstanceFunctionException {
        Object result = putFunction.apply(map, "key", "value");
        assertNull(result);
        assertEquals("value", map.get("key"));
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_NullArgs_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(null);
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_InsufficientArgs_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(map);
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_ExcessiveArgs_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(map, "key", "value", "extra");
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_TargetNotMap_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply("not a map", "key", "value");
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_NonStringProperty_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(map, 123, "value");
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_NullProperty_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(map, null, "value");
    }

    @Test(expected = RuleInstanceFunctionException.class)
    public void apply_NullValue_ThrowsException() throws RuleInstanceFunctionException {
        putFunction.apply(map, "key", null);
    }
}
