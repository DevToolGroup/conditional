package group.devtool.conditional.engine;

import group.devtool.conditional.engine.RuleInstanceException.RuleInstanceFunctionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UpperFunctionTest {

    private UpperFunction upperFunction;

    @Before
    public void setUp() {
        upperFunction = new UpperFunction();
    }

    @Test
    public void apply_ValidString_ReturnsUpperCaseFirstCharacter() throws RuleInstanceFunctionException {
        Character result = upperFunction.apply("abc");
        assertEquals(Optional.of(Character.toUpperCase('a')).get(), result);
    }

    @Test
    public void apply_NullArgs_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> upperFunction.apply((Object[]) null));
    }

    @Test
    public void apply_WrongNumberOfArgs_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> upperFunction.apply("abc", "extra arg"));
    }

    @Test
    public void apply_NonStringArg_ThrowsException() {
        assertThrows(RuleInstanceFunctionException.class, () -> upperFunction.apply(123));
    }

    @Test
    public void apply_StringWithMultipleCharacters_ReturnsFirstCharacter() throws RuleInstanceFunctionException {
        Character result = upperFunction.apply("abcd");
        assertEquals(Optional.of(Character.toUpperCase('a')).get(), result);
    }

    @Test
    public void apply_StringWithSpecialCharacters_ReturnsFirstCharacter() throws RuleInstanceFunctionException {
        Character result = upperFunction.apply("!@#$");
        assertEquals(Optional.of(Character.toUpperCase('!')).get(), result);
    }

    @Test
    public void apply_StringWithWhitespace_ReturnsWhitespaceCharacter() throws RuleInstanceFunctionException {
        Character result = upperFunction.apply("   ");
        assertEquals(Optional.of(Character.toUpperCase(' ')).get(), result);
    }
}
