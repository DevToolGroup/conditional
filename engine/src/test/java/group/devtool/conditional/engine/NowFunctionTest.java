package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class NowFunctionTest {

    private NowFunction nowFunction;

    @Before
    public void setUp() {
        nowFunction = new NowFunction();
    }

    @Test
    public void apply_NoArguments_ReturnsCurrentDate() throws RuleInstanceException.RuleInstanceFunctionException {
        Date now = new Date();
        Date result = null;
        try {
            result = nowFunction.apply();
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            fail("Exception should not have been thrown");
        }
        assertEquals("The returned date should be close to the current date", now, result);
    }

    @Test
    public void apply_WithArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        Object[] args = {"Some argument"};
        try {
            nowFunction.apply(args);
            fail("Exception should have been thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("NOW函数不需要参数", e.getMessage());
        }
    }

    @Test
    public void apply_NullArguments_ThrowsException() throws RuleInstanceException.RuleInstanceFunctionException {
        try {
            nowFunction.apply((Object[]) null);
            fail("Exception should have been thrown");
        } catch (RuleInstanceException.RuleInstanceFunctionException e) {
            assertEquals("NOW函数不需要参数", e.getMessage());
        }
    }
}
