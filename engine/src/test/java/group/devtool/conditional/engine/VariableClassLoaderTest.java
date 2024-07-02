package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.VariableClassLoader;

public class VariableClassLoaderTest {

  public List<Character> cs = new ArrayList<>();

  public String success;

  @Before
  public void read() {
    success = "CONST Integer score \"当日积分数量\" = 0\n";
    for (int i = 0; i < success.length(); i++) {
      cs.add(success.charAt(i));
    }
  }

  @Test
  public void testVariableClassLoader() {
    VariableClassLoader loader = new VariableClassLoader();
    ReteRuleClass ruleClass = new ReteRuleClass("id");
    try {
      loader.load(0, cs, ruleClass);
      VariableClass v1 = ruleClass.getVariableClass("score");
      assertTrue(null != v1);
      assertEquals("Integer", v1.getType());
      assertEquals("score", v1.getCode());
      assertEquals("当日积分数量", v1.getName());
      assertEquals("0", v1.getValueExpression().getInstance().getExpressionString());

    } catch (RuleClassException e) {
      fail(e.getMessage());
    }
  }

  
  
}
