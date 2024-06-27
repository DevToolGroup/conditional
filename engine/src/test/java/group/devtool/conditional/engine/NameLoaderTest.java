package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.NameLoader;
import group.devtool.conditional.engine.RuleClassException.RuleClassSyntaxException;

public class NameLoaderTest {

  public List<Character> simple = new ArrayList<>();
  public List<Character> error = new ArrayList<>();

  @Before
  public void read() {
    String simpleDeclaredString = "\"用户\" ";
    for (int i = 0; i < simpleDeclaredString.length(); i ++) {
      simple.add(simpleDeclaredString.charAt(i));
    }

    String errorCollectionDeclaredString = "\"用户";
    for (int i = 0; i < errorCollectionDeclaredString.length(); i ++) {
      error.add(errorCollectionDeclaredString.charAt(i));
    }


  }

  @Test
  public void testSimpleNameLoader() {
    NameLoader loader = new NameLoader();
    try {
      int end = loader.load(0, simple, null);
      assertEquals(4, end);
      assertEquals("用户", loader.pop().getValue());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }

  }


  @Test
  public void testErrorNameLoader() {
    NameLoader loader = new NameLoader();
    try {
      int end = loader.load(0, error, null);
    } catch (RuleClassException e) {
      assertTrue(e instanceof RuleClassSyntaxException);
      assertTrue(3 == ((RuleClassSyntaxException)e).getPos());
    }

  }
}
