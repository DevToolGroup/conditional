package group.devtool.conditional.engine;

import java.util.List;

/**
 * 表达式定义
 */
public interface ExpressionClass {
  
  public ExpressionInstance getInstance();

  public List<VariableReference> getVariableReference();

}
