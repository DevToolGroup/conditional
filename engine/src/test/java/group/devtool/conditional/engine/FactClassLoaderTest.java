package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.FactClassLoader;

public class FactClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success;

  @Before
  public void read() {
    success = "TYPE History \"积分历史\"";
    success += "Date    publishTime \"时间\"\n";
    success += "Integer count       \"积分数量\"\n";
    success += "END";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testFactClassLoader() {
    FactClassLoader loader = new FactClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      int end = loader.load(0, cs, ruleClass);
      assertEquals(success.length(), end);
      FactClass fact = ruleClass.getFactClass("History");
      assertEquals("History", fact.getCode());
      assertEquals("积分历史", fact.getName());
      assertEquals(2, fact.getProperties().size());

      boolean hasTime = false;
      boolean hasCount = false;
      for (FactPropertyClass property : fact.getProperties()) {
        if (property.getCode().equals("publishTime")) {
          hasTime = true;
          assertEquals("Date", property.getType());
          assertEquals("时间", property.getName());
        } 
        if (property.getCode().equals("count")) {
          hasCount = true;
          assertEquals("Integer", property.getType());
          assertEquals("积分数量", property.getName());
        }
      }
      assertTrue(hasTime);
      assertTrue(hasCount);
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
  }

}
