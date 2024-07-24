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
    CacheRuleClass ruleClass = new CacheRuleClass("id");
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
