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
 * 表达式实例接口
 */
public interface ExpressionInstance {

  public String getExpressionString();

  /**
   * 根据上下文计算表达式结果
   * 
   * @param context 上下文
   * @return 计算结果
   * @throws RuleInstanceException 规则实例相关异常
   */
  public Object getObject(RuleInstance context) throws RuleInstanceException;

  default Object getCacheObject(RuleInstance context) throws RuleInstanceException {
    String key = this.getExpressionString();
    Object value = context.getExpressionValueOrDefault(key, getObject(context));
    context.cacheExpressionValue(key, value);
    return value;
  }

}
