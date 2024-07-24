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

import java.util.Map;

/**
 * 规则实例服务
 */
public interface RuleInstanceService {

	default Map<String, Object> exec(Map<String, Object> params,
																	ConditionFunction<?>... functions)
					throws RuleInstanceException, RuleClassException {
		return exec(true, true, params, functions);
	}

	/**
	 * 根据规则定义ID，执行规则
	 *
	 * @param isConstraintArguments 是否约束参数
	 * @param isIgnoreResult 是否忽略返回结果
	 * @param params      执行参数
	 * @param functions   自定义函数
	 * @return 执行结果
	 * @throws RuleInstanceException 规则实例运行异常
	 * @throws RuleClassException    规则定义异常
	 */
	public Map<String, Object> exec(boolean isConstraintArguments, boolean isIgnoreResult,
																	Map<String, Object> params,
																	ConditionFunction<?>... functions)
					throws RuleInstanceException, RuleClassException;

}
