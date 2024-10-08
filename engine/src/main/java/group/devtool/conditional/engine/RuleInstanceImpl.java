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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link RuleInstance} Rete算法实现实例
 */
public class RuleInstanceImpl implements RuleInstance {

  private final boolean isConstraintArguments;

  private final boolean isIgnoreResult;

  private boolean initialized = false;

  private final LinkedList<Object> objects = new LinkedList<>();

  private final Map<String, Object> memory = new HashMap<>();

  private Map<String, ConditionFunction<?>> functions = new HashMap<>();

  private final RuleClass ruleClass;

  private Map<String, Object> result;

  public RuleInstanceImpl(RuleClass ruleClass) {
    this(ruleClass, true, true);
  }

  public RuleInstanceImpl(RuleClass ruleClass, boolean isConstraintArguments, boolean isIgnoreResult) {
    this.ruleClass = ruleClass;
    this.isConstraintArguments = isConstraintArguments;
    this.isIgnoreResult = isIgnoreResult;
  }

  @Override
  public Object peek() {
    return objects.peek();
  }

  @Override
  public void push(Object obj) {
    objects.push(obj);
  }

  @Override
  public void pop() {
    objects.pop();
  }

  @Override
  public ConditionFunction<?> getDeclaredFunction(String funcName) {
    return functions.get(funcName);
  }

  @Override
  public void initialized(Map<String, Object> params, ConditionFunction<?>[] functions) throws RuleInstanceException {
    if (this.initialized) {
      throw RuleInstanceException.initException("规则：" + ruleClass.getId() + " 已实例化");
    }

    if (isConstraintArguments) {
      // 校验参数
      valid(params);
    }

    // 函数注册
    this.functions = loadBuiltInFunction();
    if (null != functions) {
      for (ConditionFunction<?> function : functions) {
        this.functions.put(function.getName(), function);
      }
    }

    Map<String, Object> context = new HashMap<>(params);
    // 上下文入栈
    this.objects.push(context);
    // 全局变量计算
    global(context);
    // 结果参数构造
    this.result = new HashMap<>();
    context.put(ruleClass.getReturnClass().getCode(), result);
    this.initialized = true;
  }

  private Map<String, ConditionFunction<?>> loadBuiltInFunction() {
    return Functions.toMap();
  }

  private void global(Map<String, Object> context) throws RuleInstanceException {
    Collection<VariableClass> variables = ruleClass.getVariableClasses();
    for (VariableClass variableClass : variables) {
      Object value = variableClass.getValueExpression().getInstance().getCacheObject(this);
      context.put(variableClass.getCode(), value);
    }
  }

  private void valid(Map<String, Object> params) throws RuleInstanceException {
    for (Entry<String, Object> entry : params.entrySet()) {
      if (null == ruleClass.getArgumentClass(entry.getKey())) {
        throw RuleInstanceException.parameterException("参数不存在。 参数编码：" + entry.getKey());
      }
      ArgumentClass argumentClass = ruleClass.getArgumentClass(entry.getKey());
      FactClass factClass = ruleClass.getFactClass(argumentClass.getType());
      valid(entry.getValue(), factClass);
    }
  }

  @SuppressWarnings("unchecked")
  private void valid(Object value, FactClass factClass) throws RuleInstanceException {
    if (null == factClass) {
      throw RuleInstanceException.parameterException("参数类型不存在。");
    }
    if (!(value instanceof Map)) {
      throw RuleInstanceException
          .parameterException("参数类型异常。" + ", 类型：" + factClass.getCode());
    }

    Map<String, FactPropertyClass> pm = factClass.getProperties()
        .stream()
        .collect(Collectors.toMap(FactPropertyClass::getCode, Function.identity()));

    Map<String, Object> map = (Map<String, Object>) value;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (!pm.containsKey(entry.getKey())) {
        throw RuleInstanceException.parameterException("参数字段不支持。 参数编码：" + entry.getKey());
      }
      FactPropertyClass property = pm.get(entry.getKey());
      valid(entry.getValue(), property);
    }

  }

  @SuppressWarnings("unchecked")
  private void valid(Object value, FactPropertyClass property) throws RuleInstanceException {
    DataType dt = DataType.valueOf(property.getType());
		if (dt == DataType.List) {
			if (!(value instanceof List)) {
				throw RuleInstanceException.parameterException("参数类型异常。预期参数类型：List，实际参数类型：" + value.getClass().getSimpleName());
			}
			List<Object> lo = (List<Object>) value;
			for (Object obj : lo) {
				valid(obj, property.getValueType());
			}
		} else if (dt == DataType.Map) {
			if (!(value instanceof Map)) {
				throw RuleInstanceException.parameterException("参数类型异常。预期参数类型：Map，实际参数类型：" + value.getClass().getSimpleName());
			}
			Map<String, Object> lo = (Map<String, Object>) value;
			for (Object obj : lo.values()) {
				valid(obj, property.getValueType());
			}
		} else {
			if (!dt.getType().isAssignableFrom(value.getClass())) {
				throw RuleInstanceException.parameterException("参数类型异常。预期参数类型：" + property.getType()
						+ "，实际参数类型：" + value.getClass().getSimpleName());
			}
		}
  }

  private void valid(Object obj, String valueType) throws RuleInstanceException {
    FactClass fact = ruleClass.getFactClass(valueType);
    if (null != fact) {
      valid(obj, fact);
      return;
    }
    if (!DataType.getBaseTypes().contains(valueType)) {
      throw RuleInstanceException.parameterException("参数类型异常。预期参数类型：" + valueType
          + "，实际参数类型：" + obj.getClass().getSimpleName());
    }
    if (!DataType.valueOf(valueType).getType().isAssignableFrom(obj.getClass())) {
      throw RuleInstanceException.parameterException("参数类型异常。预期参数类型：" + valueType
          + "，实际参数类型：" + obj.getClass().getSimpleName());
    }
  }

  @Override
  public Map<String, Object> invoke() throws RuleInstanceException {
    ruleClass.getConditionGroup().invoke(this);

    if (isIgnoreResult) {
      FactClass factClass = ruleClass.getFactClass(ruleClass.getReturnClass().getType());
      // 过滤返回结果
      ignoreResult(this.result, factClass.getProperties());
    }
    return this.result;
  }

  private void ignoreResult(Map<String, Object> result, List<FactPropertyClass> properties)
      throws RuleInstanceException {
    for (FactPropertyClass propertyClass : properties) {
      if (!result.containsKey(propertyClass.getCode())) {
        result.remove(propertyClass.getCode());
        continue;
      }
      ignoreResult(result.get(propertyClass.getCode()), propertyClass);
    }
  }

  @SuppressWarnings("unchecked")
  private void ignoreResult(Object object, FactPropertyClass propertyClass) throws RuleInstanceException {
    if (null != ruleClass.getFactClass(propertyClass.getType())) {
      if (!(object instanceof Map)) {
        throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + propertyClass.getCode()
            + "，类型：" + propertyClass.getType());
      }
      Map<String, Object> result = (Map<String, Object>) object;
      FactClass factClass = ruleClass.getFactClass(propertyClass.getType());
      ignoreResult(result, factClass.getProperties());

    } else if (propertyClass.getType().equals(DataType.List.name())) {
      if (!(object instanceof List)) {
        throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + propertyClass.getCode()
            + "，类型：" + propertyClass.getType());
      }
      List<Object> lo = (List<Object>) object;
      for (Object result : lo) {
        ignoreResult(result, propertyClass.getValueType(), propertyClass.getCode());
      }
    } else if (propertyClass.getType().equals(DataType.Map.name())) {
      if (!(object instanceof Map)) {
        throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + propertyClass.getCode()
            + "，类型：" + propertyClass.getType());
      }
      Map<String, Object> lo = (Map<String, Object>) object;
      for (Object result : lo.values()) {
        ignoreResult(result, propertyClass.getValueType(), propertyClass.getCode());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void ignoreResult(Object result, String valueType, String code) throws RuleInstanceException {
    if (DataType.getBaseTypes().contains(valueType)) {
      if (DataType.valueOf(valueType).getType().isAssignableFrom(result.getClass())) {
        return;
      }
      throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + code
          + "，类型：" + valueType);
    }
    if (valueType.equals(DataType.List.name())) {
      // FIX ME： 处理多级嵌套的List，Map类型
      throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + code
          + "，类型：" + valueType);
    }
    if (valueType.equals(DataType.Map.name())) {
      // FIX ME： 处理多级嵌套的List，Map类型
      throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + code
          + "，类型：" + valueType);
    }

    FactClass fact = ruleClass.getFactClass(valueType);
    if (null == fact) {
      throw RuleInstanceException.returnException("返回结果不符合定义。字段：" + code
          + "，类型：" + valueType);
    }
    ignoreResult((Map<String, Object>) result, fact.getProperties());

  }

  @Override
  public Object computeExpressionValueIfAbsent(String key, ExpressionInstance.ExpressionCacheSupplier supplier) throws RuleInstanceException {
    Object v;
    if ((v = memory.get(key)) == null) {
      Object newValue;
      if ((newValue = supplier.get()) != null) {
        memory.put(key, newValue);
        return newValue;
      }
    }
    return v;
  }

}
