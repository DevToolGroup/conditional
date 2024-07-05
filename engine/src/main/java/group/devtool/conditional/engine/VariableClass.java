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
 * 全局变量定义
 */
public interface VariableClass {

  /**
   * @return 变量类型
   */
  public String getType();

  public String getKeyType();

  public String getValueType();

  /**
   * @return 变量编码
   */
  public String getCode();

  /**
   * @return 变量名称
   */
  public String getName();

  /**
   * @return 变量值表达式
   */
  public ExpressionClass getValueExpression();
}
