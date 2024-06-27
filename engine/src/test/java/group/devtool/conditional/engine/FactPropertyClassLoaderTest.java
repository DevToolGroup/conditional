package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.FactPropertyClassLoader;

public class FactPropertyClassLoaderTest {
  
  public List<Character> cs = new ArrayList<>();

  @Before
  public void read() {
    String success = "Integer id \"用户ID\"\n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testFactPropertyClassLoader() {
    FactPropertyClassLoader loader = new FactPropertyClassLoader();
    try {
      int end = loader.load(0, cs, null);
      assertEquals(17, end);
      FactPropertyClass value = loader.pop();
      assertEquals("Integer", value.getType());
      assertEquals("id", value.getCode());
      assertEquals("用户ID", value.getName());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }

}
