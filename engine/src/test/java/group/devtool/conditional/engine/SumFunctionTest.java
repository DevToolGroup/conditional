package group.devtool.conditional.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SumFunctionTest {

    private SumFunction sumFunction;

    @Before
    public void setUp() {
        sumFunction = new SumFunction();
    }

    @Test
    public void apply_NullArgs_ThrowsException() {
        try {
            sumFunction.apply(null);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("SUM函数需要一个列表类型参数，其元素类型为数值类型", e.getMessage());
        }
    }

    @Test
    public void apply_EmptyArgs_ThrowsException() {
        try {
            sumFunction.apply();
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("SUM函数需要一个列表类型参数，其元素类型为数值类型", e.getMessage());
        }
    }

    @Test
    public void apply_NotListArg_ThrowsException() {
        try {
            sumFunction.apply("not a list");
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("SUM函数需要一个列表类型参数", e.getMessage());
        }
    }

    @Test
    public void apply_ListWithNonNumberArg_ThrowsException() {
        try {
            sumFunction.apply(Arrays.asList("not a number"));
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("SUM函数列表元素类型非数值类型", e.getMessage());
        }
    }

    @Test
    public void apply_IntegerList_ReturnsSum() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Number result = sumFunction.apply(list);
        assertEquals(15, result.intValue());
    }

    @Test
    public void apply_MixedNumberList_ReturnsSum() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Number> list = Arrays.asList(1, 2.0, 3.5f, 4, new BigDecimal("5.75"));
        Number result = sumFunction.apply(list);
        assertEquals(16, result.intValue());
    }

    @Test
    public void apply_EmptyList_ReturnsZero() throws RuleInstanceException.RuleInstanceFunctionException {
        List<Number> list = Collections.emptyList();
        Number result = sumFunction.apply(list);
        assertEquals(0, result.intValue());
    }

    @Test
    public void apply_ListWithUnsupportedType_ThrowsException() {
        List<Number> list = Arrays.asList(1, 2, new BigInteger("3"));
        try {
            sumFunction.apply(list);
            fail("Expected RuleInstanceFunctionException to be thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("SUM函数列表元素数值类型不支持", e.getMessage());
        }
    }
}
