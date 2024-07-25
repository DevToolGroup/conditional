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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import group.devtool.conditional.engine.VariableReference.NestVariableReference;
import group.devtool.conditional.engine.VariableReference.SimpleVariableReference;

/**
 * {@link RuleClass} 基本实现
 */
public abstract class AbstractRuleClass implements RuleClass {

  private Map<String, FactClass> factClasses;

  private Map<String, ArgumentClass> argumentClasses;

  private ReturnClass returnClass;

  private Map<String, VariableClass> variableClasses;

  private List<VariableClass> orderVariableClasses;

  private String id;

  public AbstractRuleClass() {

  }

  public AbstractRuleClass(String id) {
    this.id = id;
    this.factClasses = new HashMap<>();
    this.argumentClasses = new HashMap<>();
    this.variableClasses = new HashMap<>();
    this.orderVariableClasses = new ArrayList<>();
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public Collection<FactClass> getFactClasses() {
    return factClasses.values();
  }

  public void setFactClasses(Map<String, FactClass> factClasses) throws RuleClassException {
    this.factClasses = factClasses;
  }

  @Override
  public FactClass getFactClass(String code) {
    return factClasses.get(code);
  }

  public void addFactClass(FactClass factClass) throws RuleClassException {
    if (factClasses.containsKey(factClass.getCode())) {
      throw RuleClassException.syntaxException("类型重复定义，类型编码：" + factClass.getCode());
    }
    factClasses.put(factClass.getCode(), factClass);
  }

  @Override
  public Collection<ArgumentClass> getArgumentClasses() {
    return argumentClasses.values();
  }

  @Override
  public ArgumentClass getArgumentClass(String code) {
    return argumentClasses.get(code);
  }

  public void addArgumentClasses(List<ArgumentClass> argumentClasses) throws RuleClassException {
    for (ArgumentClass argumentClass : argumentClasses) {
      if (this.argumentClasses.containsKey(argumentClass.getCode())) {
        throw RuleClassException.syntaxException("输入参数类型编码重复， 编码：" + argumentClass.getCode());
      }
      this.argumentClasses.put(argumentClass.getCode(), argumentClass);
    }
  }

  @Override
  public ReturnClass getReturnClass() {
    return returnClass;
  }

  public void setReturn(ReturnClass returnClass) {
    this.returnClass = returnClass;
  }

  @Override
  public List<VariableClass> getVariableClasses() {
    return orderVariableClasses;
  }

  // setter variableClasses
  public void setVariableClasses(List<VariableClass> variableClasses) throws RuleClassException {
    this.variableClasses = variableClasses.stream().collect(Collectors.toMap(VariableClass::getCode, i -> i));
    this.orderVariableClasses = variableClasses;
  }

  public void addVariableClass(VariableClass variableClass) {
    orderVariableClasses.add(variableClass);
    this.variableClasses.put(variableClass.getCode(), variableClass);
  }

  @Override
  public VariableClass getVariableClass(String code) {
    return variableClasses.get(code);
  }

  public void addConditionClass(ConditionClass conditionClass) {
    getConditionGroup().addCondition(conditionClass);
  }

  /**
   * 检查定义是否符合以下要求：
   * 
   * 1. 检查类型定义，字段属性是否为基本类型，List，Set是否声明元素类型，Map类型中的key只能是基本类型，不能为声明类型
   * 2. 检查输入参数类型定义
   * 3. 检查返回结果类型定义，是否已声明
   * 4. 检查变量定义中的引用变量，是否是声明的变量，或者输入参数
   * 5. 检查规则条件中的引用变量，是否是声明的变量，或者输入参数
   * 6. 检查规则动作中的引用变量，是否是返回结果类型
   * 
   * @throws RuleClassException 语法异常
   */
  public void completed() throws RuleClassException {
    // 0. condition group complete
    getConditionGroup().completed();

    // 1. 校验字段
    checkFacts();
    // 2. 校验输入参数
    checkArguments();
    // 3. 校验返回结果
    checkReturn();
    // 4. 校验变量
    checkVariables();
    // 5. 校验规则中的变量
    checkConditions();
    // 6. 校验变量编码，全局唯一
    checkGlobalCode();

  }

  private void checkGlobalCode() throws RuleClassException {
    Set<String> argCodes = argumentClasses.keySet();
    Set<String> varCodes = variableClasses.keySet();

    if (argCodes.containsAll(varCodes)) {
      throw RuleClassException.syntaxException("输入参数与全局变量的编码存在重复。编码：" + String.join(",", argCodes));
    }

    String returnCode = returnClass.getCode();

    if (argCodes.contains(returnCode)) {
      throw RuleClassException.syntaxException("返回结果变量编码与输入参数的编码存在重复。编码：" + returnCode);
    }
    if (varCodes.contains(returnCode)) {
      throw RuleClassException.syntaxException("返回结果变量编码与全局变量的编码存在重复。编码：" + returnCode);
    }

  }

  private void checkConditions() throws RuleClassException {
    List<ConditionClass> conditions = getConditionGroup().getConditions();

    List<ExpressionClass> conditionExpressions = conditions.stream().map(i -> i.getCondition())
        .collect(Collectors.toList());

    for (ExpressionClass expressionClass : conditionExpressions) {
      checkReference(expressionClass.getVariableReferences(), false);
    }

    List<ExpressionClass> actionExpressions = conditions.stream().map(i -> i.getFunctions()).flatMap(List::stream)
        .collect(Collectors.toList());
    for (ExpressionClass expressionClass : actionExpressions) {
      checkReference(expressionClass.getVariableReferences(), true);
    }
  }

  private void checkVariables() throws RuleClassException {
    Set<String> system = DataType.getSystemTypes();

    Collection<VariableClass> variables = getVariableClasses();
    for (VariableClass variable : variables) {
      if (!system.contains(variable.getType()) && !factClasses.containsKey(variable.getType())) {
        throw RuleClassException.syntaxException("变量类型不存在。参数编码："
            + variable.getCode()
            + "，参数类型：" + variable.getType());
      }

      // 校验变量引用是否符合
      checkReference(variable.getValueExpression().getVariableReferences(), false);
    }
  }

  private void checkReference(List<VariableReference> variableReference, boolean onlyReturn) throws RuleClassException {
    for (VariableReference reference : variableReference) {
      checkReference(reference, onlyReturn);
    }
  }

  private void checkReference(VariableReference reference, boolean onlyReturn) throws RuleClassException {
    if (reference instanceof SimpleVariableReference) {
      SimpleVariableReference simple = (SimpleVariableReference) reference;
      String varName = simple.getName();
      if (!onlyReturn) {
        if (!argumentClasses.containsKey(varName) && !variableClasses.containsKey(varName)) {
          throw RuleClassException.syntaxException("输入参数/全局变量中不存在表达式的引用变量。变量名：" + varName);
        }
      } else {
        if (!returnClass.getCode().equals(varName)) {
          throw RuleClassException.syntaxException("返回结果不存在表达式的引用变量。变量名：" + varName);
        }
      }
    }
    FactClass fact;
    if (reference instanceof NestVariableReference) {
      NestVariableReference nest = (NestVariableReference) reference;
      String varName = nest.getName();
      if (!onlyReturn) {
        if (!argumentClasses.containsKey(varName) && !variableClasses.containsKey(varName)) {
          throw RuleClassException.syntaxException("输入参数/全局变量中不存在表达式的引用变量。变量名：" + nest.getName());
        }
        ArgumentClass argumentClass = argumentClasses.get(varName);
        fact = factClasses.get(argumentClass.getType());
      } else {
        if (!returnClass.getCode().equals(varName)) {
          throw RuleClassException.syntaxException("返回结果定义中不存在表达式的引用变量。变量名：" + nest.getName());
        }
        fact = factClasses.get(returnClass.getType());
      }

      boolean isArray = false;
      boolean isMap = false;

      Iterator<String> iterator = nest.iterator();
      while (iterator.hasNext()) {
        if (null == fact) {
          throw RuleClassException.syntaxException("属性不存在");
        }
        String child = iterator.next();
        if (isArray || isMap) {
          isArray = false;
          isMap = false;
          continue;
        }
        FactPropertyClass property = getPropertyType(child, fact, isArray, isMap);
        if (null == property) {
          throw RuleClassException.syntaxException("属性不存在");
        }
        if (factClasses.containsKey(property.getType())) {
          fact = factClasses.get(property.getType());
        } else if (property.getType().equals(DataType.List.name())) {
          isArray = true;
          fact = factClasses.get(property.getValueType());
        } else if (property.getType().equals(DataType.Map.name())) {
          isMap = true;
          fact = factClasses.get(property.getValueType());
        } else {
          fact = null;
        }

      }

    }
  }

  private FactPropertyClass getPropertyType(String child, FactClass fact, boolean isArray, boolean isMap) {
    List<FactPropertyClass> properties = fact.getProperties();
    for (FactPropertyClass property : properties) {
      if (property.getCode().equals(child)) {
        return property;
      }
    }
    return null;
  }

  private void checkReturn() throws RuleClassException {
    if (!factClasses.containsKey(returnClass.getType())) {
      throw RuleClassException.syntaxException("返回结果类型不存在。参数编码："
          + returnClass.getCode()
          + "，参数类型：" + returnClass.getType());
    }
  }

  private void checkArguments() throws RuleClassException {
    Set<String> system = DataType.getSystemTypes();

    Set<String> exists = new HashSet<>();

    Collection<ArgumentClass> arguments = getArgumentClasses();
    for (ArgumentClass argument : arguments) {
      if (!system.contains(argument.getType()) && !factClasses.containsKey(argument.getType())) {
        throw RuleClassException.syntaxException("输入参数类型不存在。参数编码："
            + argument.getCode()
            + "，参数类型：" + argument.getType());
      }
      if (exists.contains(argument.getCode())) {
        throw RuleClassException.syntaxException("输入参数编码重复。参数编码："
            + argument.getCode());
      }
      exists.add(argument.getCode());
    }
  }

  private void checkFacts() throws RuleClassException {
    Collection<FactClass> facts = getFactClasses();
    for (FactClass fact : facts) {
      checkProperties(fact.getCode(), fact.getProperties());
    }
  }

  private void checkProperties(String code, List<FactPropertyClass> properties) throws RuleClassException {
    Set<String> exists = new HashSet<>();

    Set<String> system = DataType.getSystemTypes();
    Set<String> base = DataType.getBaseTypes();

    for (FactPropertyClass property : properties) {
      String propertyCode = property.getCode();

      if (exists.contains(propertyCode)) {
        throw RuleClassException.syntaxException("事实属性编码定义重复。编码：" + propertyCode + " 事实编码：" + code);
      }
      exists.add(propertyCode);

      String propertyType = property.getType();
      if (!system.contains(propertyType) && !factClasses.containsKey(propertyType)) {
        throw RuleClassException.syntaxException(
            "事实属性类型仅支持基本类型及声明类型。"
                + "类型：" + propertyType
                + "，编码：" + propertyCode
                + " ，事实编码：" + code);
      }
      if (DataType.List.name().equals(propertyType)
          || DataType.Map.name().equals(propertyType)) {
        if (null == property.getValueType()) {
          throw RuleClassException.syntaxException(
              "事实属性集合类型需指定元素类型。"
                  + "类型：" + propertyType
                  + "，编码：" + propertyCode
                  + " ，事实编码：" + code);
        }
        if (!system.contains(property.getValueType()) && !factClasses.containsKey(property.getValueType())) {
          throw RuleClassException.syntaxException(
              "事实属性容器类型仅支持基本类型及声明类型。"
                  + "类型：" + propertyType
                  + "，编码：" + propertyCode
                  + " ，事实编码：" + code);
        }
        if (DataType.Map.name().equals(propertyType)) {
          if (null == property.getKeyType()) {
            throw RuleClassException.syntaxException(
                "事实属性Map类型需指定Key的类型。"
                    + "类型：" + propertyType
                    + "，编码：" + propertyCode
                    + " ，事实编码：" + code);
          }
          if (!base.contains(property.getKeyType())) {
            throw RuleClassException.syntaxException(
                "事实属性Map类型的Key类型只支持基本类型。"
                    + "类型：" + propertyType
                    + "，编码：" + propertyCode
                    + " ，事实编码：" + code);
          }
        }
      }
    }
  }

}
