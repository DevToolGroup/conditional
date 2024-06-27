package group.devtool.conditional.engine;

import java.util.Collection;

/**
 * 规则定义
 */
public interface RuleClass {

  public String getId();

  public Collection<FactClass> getFactClasses();

  public FactClass getFactClass(String code);

  public Collection<ArgumentClass> getArgumentClasses();

  public ArgumentClass getArgumentClass(String code);

  public ReturnClass getReturnClass();

  public Collection<VariableClass> getVariableClasses();  

  public VariableClass getVariableClass(String code);  

  public ConditionClassGroup getConditionGroup();

}
