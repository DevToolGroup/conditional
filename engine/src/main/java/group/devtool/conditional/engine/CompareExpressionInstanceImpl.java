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

import java.math.BigDecimal;

import group.devtool.conditional.engine.Operation.Compare;

/**
 * {@link CompareExpressionInstance} 默认实现
 */
public class CompareExpressionInstanceImpl implements CompareExpressionInstance {

	private ExpressionInstance left;

	private Compare operation;

	private ExpressionInstance right;

	public CompareExpressionInstanceImpl(ExpressionInstance left, Compare compare, ExpressionInstance right) {
		this.left = left;
		this.operation = compare;
		this.right = right;

	}

	@Override
	public Object getObject(RuleInstance context) throws RuleInstanceException {
		Object lv = left.getCacheObject(context);
		Object rv = right.getCacheObject(context);

		if (operation == Compare.GT) {
			return gt(lv, rv);
		}
		if (operation == Compare.GE) {
			return ge(lv, rv);
		}
		if (operation == Compare.LT) {
			return lt(lv, rv);
		}
		if (operation == Compare.LE) {
			return le(lv, rv);
		}
		if (operation == Compare.EQ) {
			return eq(lv, rv);
		}
		if (operation == Compare.NE) {
			return ne(lv, rv);
		}
		throw RuleInstanceException.runtimeException("不支持比较操作符");
	}

	private Object ne(Object lv, Object rv) {
		if (lv == null && rv == null) {
			return true;
		} else if (rv == null) {
			return false;
		} else if (lv instanceof String) {
			return !lv.equals(rv);
		} else {
			return lv != rv;
		}
	}

	private Object eq(Object lv, Object rv) {
		if (lv == null && rv == null) {
			return true;
		} else if (rv == null) {
			return false;
		} else if (lv instanceof String) {
			return lv.equals(rv);
		} else {
			return lv == rv;
		}
	}

	private boolean le(Object lv, Object rv) throws RuleInstanceException {
		if (null == lv || null == rv) {
			return false;
		}
		return compare(lv, rv) <= 0;
	}

	private Object lt(Object lv, Object rv) throws RuleInstanceException {
		if (null == lv || null == rv) {
			return false;
		}
		return compare(lv, rv) < 0;
	}

	private Object ge(Object lv, Object rv) throws RuleInstanceException {
		if (null == lv || null == rv) {
			return false;
		}
		return compare(lv, rv) >= 0;
	}

	private Object gt(Object lv, Object rv) throws RuleInstanceException {
		if (null == lv || null == rv) {
			return false;
		}
		return compare(lv, rv) > 0;
	}

	@SuppressWarnings("unchecked")
	private int compare(Object lv, Object rv) throws RuleInstanceException {
		if (lv instanceof Number && rv instanceof Number) {
			return new BigDecimal(lv.toString()).compareTo(new BigDecimal(rv.toString()));
		}
		if (lv instanceof Comparable && rv instanceof Comparable) {
			if (lv.getClass().equals(rv.getClass())) {
				Comparable<Object> left = (Comparable<Object>) lv;
				Comparable<Object> right = (Comparable<Object>) rv;
				return left.compareTo(right);
			} else {
				throw RuleInstanceException.unexpectedException(String.format(
								"语句执行结果不符合预期，类型不匹配。预期类型：Comparable类型，实际类型：左侧为%s，右侧为%s",
								lv.getClass().getSimpleName(), rv.getClass().getSimpleName()
				));
			}
		}
		throw RuleInstanceException.unexpectedException("语句执行结果不符合预期，预期类型：Comparable类型");
	}

	@Override
	public ExpressionInstance left() {
		return left;
	}

	@Override
	public ExpressionInstance right() {
		return right;
	}

	@Override
	public String getExpressionString() {
		return left.getExpressionString() + operation.op() + right.getExpressionString();
	}

	@Override
	public Compare getCompare() {
		return operation;
	}

}
