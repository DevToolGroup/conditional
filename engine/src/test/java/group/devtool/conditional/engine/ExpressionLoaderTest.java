package group.devtool.conditional.engine;

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

  @Before
  public void read() {
    success = "user.id != 1 && user.id == order.userId || (order.amount > 100 && order.amount < 500) && (currentDayScore < 10000 || 3 * 5.1b < 20) && true && !var \n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testExpressionLoader() {
    ExpressionLoader loader = new ExpressionLoader();
    try {
      int end = loader.load(0, cs, null);
      ExpressionToken token = loader.pop();
      List<Token> tokens = token.getTokens();
      System.out.println(tokens);
    } catch (RuleClassException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }

}
