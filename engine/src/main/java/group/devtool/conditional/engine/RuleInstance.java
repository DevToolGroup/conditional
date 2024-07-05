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

import java.util.Map;

/**
 * 规则实例上下文
 */
public interface RuleInstance {

  /**
   * 初始化缓存实例
   * 
   * @param params 输入参数
   * @param functions 自定义及系统默认函数
   * @throws RuleInstanceException 
   */
  void initialized(Map<String, Object> params, ConditionFunction<?>[] functions) throws RuleInstanceException;

  /**
   * 查询声明的函数
   * 
   * @param funcName 函数名
   * @return 函数定义
   */
  ConditionFunction<?> getDeclaredFunction(String funcName);

  /**
   * 执行规则实例
   * 
   * @return 规则执行结果
   * @throws RuleInstanceException 规则实例相关异常
   */
  Map<String, Object> invoke() throws RuleInstanceException;

  // 临时变量堆栈

  /**
   * @return 临时变量值
   */
  Object peek();

  /**
   * @return 暂存临时变量值
   */
  void push(Object obj);

  /**
   * @return 临时变量值出栈
   */
  void pop();

  // 规则结果缓存

  /**
   * 查询表达式缓存结果
   * 
   * @param key 表达式唯一值
   * @param object 默认值
   * @return 表达式缓存结果
   */
  Object getExpressionValueOrDefault(String key, Object object);

  /**
   * 缓存表达式执行结果
   * 
   * @param key 表达式唯一值
   * @param value 表达式执行结果
   */
  void cacheExpressionValue(String key, Object value);

}
