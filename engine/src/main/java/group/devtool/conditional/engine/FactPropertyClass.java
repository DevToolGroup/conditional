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
 * 事实属性定义
 */
public interface FactPropertyClass {

  /**
   * @return 事实属性类型
   */
  public String getType();

  /**
   * @return 集合类型，Map类型，需要定义 ValueType
   */
  public String getValueType();

  /**
   * @return Map类型，需要定义 KeyType
   */
  public String getKeyType();

  /**
   * @return 事实属性编码
   */
  public String getCode();

  /**
   * @return 事实属性名称
   */
  public String getName();

}
