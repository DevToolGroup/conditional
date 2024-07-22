package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MapFunctionTest {

    private MapFunction mapFunction;

    @Before
    public void setUp() {
        // Any setup code if needed in future
        mapFunction = new MapFunction();
    }

    @Test
    public void apply_ValidArguments_ShouldReturnMap() throws RuleInstanceException.RuleInstanceFunctionException {
        Object[] args = {"key1", "value1", "key2", "value2"};
        Map<Object, Object> result = mapFunction.apply(args);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void apply_NullArguments_ShouldThrowException() {
        assertThatThrownBy(() -> mapFunction.apply(null))
                .isInstanceOf(RuleInstanceException.RuleInstanceFunctionException.class)
                .hasMessage("MAP函数的参数个数需要是2的倍数");
    }

    @Test
    public void apply_InvalidNumberOfArguments_ShouldThrowException() {
        Object[] args = {"key1", "value1", "key2"};
        assertThatThrownBy(() -> mapFunction.apply(args))
                .isInstanceOf(RuleInstanceException.RuleInstanceFunctionException.class)
                .hasMessage("MAP函数的参数个数需要是2的倍数");
    }

    @Test
    public void apply_EmptyArguments_ShouldThrowException() {
        assertThatThrownBy(() -> mapFunction.apply())
                .isInstanceOf(RuleInstanceException.RuleInstanceFunctionException.class)
                .hasMessage("MAP函数的参数个数需要是2的倍数");
    }

    @Test
    public void apply_ValidArgumentsWithNullValues_ShouldReturnMapWithNullValues() throws RuleInstanceException.RuleInstanceFunctionException {
        Object[] args = {"key1", null, "key2", "value2"};
        Map<Object, Object> result = mapFunction.apply(args);

        assertEquals(2, result.size());
        assertNull(result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}
