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

/**
 * 参考RETE算法实现的规则定义加载器
 */
public class CacheRuleClassLoader extends AbstractRuleClassLoader {

  private String id;

  public CacheRuleClassLoader(String id) {
    super();
    this.id = id;
  }

  @Override
  protected AbstractRuleClass buildRuleClass() {
    return new CacheRuleClass(id);
  }
  
}