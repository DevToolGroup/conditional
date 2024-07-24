/*
 * The Conditional rule engine, similar to Drools, 
 * introduces the definition of input and output parameters, 
 * thereby demarcating the boundaries between programmers and business personnel. 
 * 
 * It reduces the complexity of rules, making it easier for business staff to maintain and use them.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
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
    CacheRuleClass ruleClass = new CacheRuleClass("id");
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
