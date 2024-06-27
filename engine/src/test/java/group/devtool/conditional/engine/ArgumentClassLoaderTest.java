package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.ArgumentClassLoader;

public class ArgumentClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  @Before
  public void before() {
    String success = "ARG Order order, User user\n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testLoadArgument() {
    ArgumentClassLoader loader = new ArgumentClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      int end = loader.load(0, cs, ruleClass);
      Collection<ArgumentClass> arguments = ruleClass.getArgumentClasses();

      assertEquals(2, arguments.size());
      ArgumentClass[] args = arguments.toArray(new ArgumentClass[] {});
      assertEquals("Order", args[1].getType());
      assertEquals("order", args[1].getCode());
      assertEquals("User", args[0].getType());
      assertEquals("user", args[0].getCode());

      assertEquals(end, 26);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }
}
