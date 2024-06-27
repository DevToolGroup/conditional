package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.ReturnClassLoader;

public class ReturnClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success;

  @Before
  public void before() {
    success = "RETURN Score score\n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testReturnClassLoader() {
    ReturnClassLoader loader = new ReturnClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      int end = loader.load(0, cs, ruleClass);
      assertEquals(success.length() - 1, end);
      ReturnClass returnClass = ruleClass.getReturnClass();

      assertEquals("Score", returnClass.getType());
      assertEquals("score", returnClass.getCode());

    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
  }

}
