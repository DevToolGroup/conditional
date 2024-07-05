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
