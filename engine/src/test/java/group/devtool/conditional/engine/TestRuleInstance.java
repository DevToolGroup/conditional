package group.devtool.conditional.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TestRuleInstance implements  RuleInstance {

	private LinkedList<Object> objects = new LinkedList<>();

	private Map<String, Object> cache = new HashMap<>();

	public TestRuleInstance(Map<String, Object> params) {
		this.objects.push(params);
	}

	@Override
	public void initialized(Map<String, Object> params, ConditionFunction<?>[] functions) throws RuleInstanceException {

	}

	@Override
	public ConditionFunction<?> getDeclaredFunction(String funcName) {
		return Functions.toMap().get(funcName);
	}

	@Override
	public Map<String, Object> invoke() throws RuleInstanceException {
		return Collections.emptyMap();
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
	public Object computeExpressionValueIfAbsent(String key, ExpressionInstance.ExpressionCacheSupplier supplier) throws RuleInstanceException {
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			return supplier.get();
		}
	}
}
