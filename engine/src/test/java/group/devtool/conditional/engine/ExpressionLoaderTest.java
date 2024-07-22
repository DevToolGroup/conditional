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

import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionLoader;
import group.devtool.conditional.engine.AbstractRuleClassLoader.ExpressionToken;

public class ExpressionLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success;

  private String negative;

  public List<Character> ns = new ArrayList<>();

  @Before
  public void read() {
    success = "user.id != \"1\" && user.id != 1 && user.id == order.userId || (order.amount > 100 && order.amount < 500) && (currentDayScore < 10000 || 3 * 5.1b < 20) && true && !var \n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }

    negative = "user.id != -1\n";
    for (int i = 0; i < negative.length(); i++) {
      ns.add(negative.charAt(i));
    }
  }

  @Test
  public void testExpressionLoader() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, cs, null);
      ExpressionToken token = loader.pop();
      List<Token> tokens = token.getTokens();
      assertEquals(50, tokens.size());
      assertEquals(TokenKind.STRING, tokens.get(4).getKind());
      assertEquals("\"1\"", tokens.get(4).getValue());
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }

  @Test
  public void testNegativeExpressionLoader() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      loader.load(0, ns, null);
      ExpressionToken token = loader.pop();
      List<Token> tokens = token.getTokens();
      assertEquals(TokenKind.MINUS, tokens.get(4).getKind());
    } catch (RuleClassException e) {
			throw new RuntimeException(e);
		}
	}

}
