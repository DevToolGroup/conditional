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

import group.devtool.conditional.engine.AbstractRuleClassLoader.CodeLoader;
import group.devtool.conditional.engine.RuleClassException.RuleClassSyntaxException;

public class CodeLoaderTest {

  public List<Character> simple = new ArrayList<>();
  public List<Character> error1 = new ArrayList<>();
  public List<Character> error2 = new ArrayList<>();
  public List<Character> error3 = new ArrayList<>();

  @Before
  public void read() {
    String simple1 = "user ";
    for (int i = 0; i < simple1.length(); i++) {
      simple.add(simple1.charAt(i));
    }

    String err1 = "user name";
    for (int i = 0; i < err1.length(); i++) {
      error1.add(err1.charAt(i));
    }

    String err2 = "1User ";
    for (int i = 0; i < err2.length(); i++) {
      error2.add(err2.charAt(i));
    }

    String err3 = "user_Name ";
    for (int i = 0; i < err3.length(); i++) {
      error3.add(err3.charAt(i));
    }

  }

  @Test
  public void testSimpleCodeLoader() {
    CodeLoader loader = new CodeLoader();
    try {
      int end = loader.load(0, simple, null);
      assertEquals(4, end);
      assertEquals("user", loader.pop().getValue());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testErrorCodeLoader1() {
    CodeLoader loader = new CodeLoader();
    try {
      int end = loader.load(0, error1, null);
      assertEquals(4, end);
      assertEquals("user", loader.pop().getValue());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testErrorCodeLoader2() {
    CodeLoader loader = new CodeLoader();
    try {
      loader.load(0, error2, null);
    } catch (RuleClassException e) {
      assertTrue(e instanceof RuleClassSyntaxException);
    }

  }

  @Test
  public void testErrorCodeLoader3() {
    CodeLoader loader = new CodeLoader();
    try {
      loader.load(0, error3, null);
    } catch (RuleClassException e) {
      assertTrue(e instanceof RuleClassSyntaxException);
    }

  }

}
