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
