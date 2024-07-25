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

import java.io.Serializable;
import java.util.List;

/**
 * 事实类型定义
 */
public interface FactClass extends Serializable {

  /**
   * @return 事实编码
   */
  String getCode();

  /**
   * @return 事实名称
   */
  String getName();

  /**
   * @return 事实字段
   */
  List<FactPropertyClass> getProperties();

}
