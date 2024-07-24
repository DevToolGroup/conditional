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
import java.util.List;

/**
 * 正常规则合并
 */
public class ConditionClassGroupImpl implements ConditionClassGroup {

	private final List<ConditionClass> conditions;

	public ConditionClassGroupImpl() {
		conditions = new ArrayList<>();
	}

	@Override
	public void addCondition(ConditionClass conditionClass) {
		conditions.add(conditionClass);
	}

	@Override
	public void invoke(RuleInstance instance) throws RuleInstanceException {
		for (ConditionClass conditionClass : conditions) {
			conditionClass.getCondition().getInstance().getCacheObject(instance);
		}
	}

	@Override
	public void completed() {
		// do nothing
	}

	@Override
	public List<ConditionClass> getConditions() {
		return conditions;
	}

}
