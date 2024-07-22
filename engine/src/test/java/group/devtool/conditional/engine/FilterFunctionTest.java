package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FilterFunctionTest {

    private FilterFunction filterFunction;

    private List<Object> testData;

    @Before
    public void setUp() {
        testData = new ArrayList<>();
        testData.add(createTestMap("name", "Tom", "age", 18));
        testData.add(createTestMap("name", "Jerry", "age", 20));
        testData.add(createTestMap("name", "Spike", "age", 22));
        filterFunction = new FilterFunction();
    }

    @Test
    public void apply_ValidArguments_ReturnsFilteredList() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Object> result = filterFunction.apply(testData, "name", "Tom");
        assertEquals(1, result.size());
        assertEquals("Tom", ((Map<?, ?>) result.get(0)).get("name"));
    }

    @Test
    public void apply_InvalidArguments_ThrowsException() {
        try {
            filterFunction.apply(null);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("FILTER函数需要一个列表类型参数，一个字符串属性参数，一个比较对象参数", e.getMessage());
        }

        try {
            filterFunction.apply(testData, 123, "Tom");
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("FILTER函数需要一个列表类型参数，一个字符串属性参数，一个比较对象参数", e.getMessage());
        }

    }

    @Test
    public void apply_EmptyList_ReturnsEmptyList() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Object> emptyList = new ArrayList<>();
        List<Object> result = filterFunction.apply(emptyList, "name", "Tom");
        assertTrue(result.isEmpty());
    }

    @Test
    public void apply_NoMatchingElement_ReturnsEmptyList() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Object> result = filterFunction.apply(testData, "name", "Mickey");
        assertTrue(result.isEmpty());
    }

    private Map<String, Object> createTestMap(Object... keyValuePairs) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put((String) keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return map;
    }
}
