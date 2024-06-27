package group.devtool.conditional.engine;

import java.util.List;

/**
 * 事实类型定义
 */
public interface FactClass {

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
