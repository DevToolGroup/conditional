package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LowerFunctionTest {

    private LowerFunction lowerFunction;

    @Before
    public void setUp() {
        lowerFunction = new LowerFunction();
    }

    @Test
    public void apply_ValidString_ReturnsLowercaseCharacter() throws RuleInstanceFunctionException {
        Character result = lowerFunction.apply("ABC");
        assertEquals(Optional.of(Character.toLowerCase('A')).get(), result);
    }

    @Test
    public void apply_NullString_ThrowsException() {
        RuleInstanceFunctionException exception = assertThrows(
                RuleInstanceFunctionException.class,
                () -> lowerFunction.apply((String) null)
        );
        assertEquals("LOWER函数只需要一个字符串", exception.getMessage());
    }

    @Test
    public void apply_NullArguments_ThrowsException() {
        RuleInstanceFunctionException exception = assertThrows(
                RuleInstanceFunctionException.class,
                () -> lowerFunction.apply((Object[]) null)
        );
        assertEquals("LOWER函数只需要一个字符串", exception.getMessage());
    }

    @Test
    public void apply_MultipleArguments_ThrowsException() {
        RuleInstanceFunctionException exception = assertThrows(
                RuleInstanceFunctionException.class,
                () -> lowerFunction.apply("ABC", "DEF")
        );
        assertEquals("LOWER函数只需要一个字符串", exception.getMessage());
    }

    @Test
    public void apply_NonStringArgument_ThrowsException() {
        RuleInstanceFunctionException exception = assertThrows(
                RuleInstanceFunctionException.class,
                () -> lowerFunction.apply(123)
        );
        assertEquals("LOWER函数只需要一个字符串", exception.getMessage());
    }

    @Test
    public void apply_StringWithMultipleCharacters_ReturnsFirstLowercaseCharacter() throws RuleInstanceFunctionException {
        Character result = lowerFunction.apply("aBc");
        assertEquals(Optional.of(Character.toLowerCase('a')).get(), result);
    }
}
