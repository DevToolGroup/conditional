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
import java.math.BigInteger;

/**
 * {@link NumberExpressionInstance} 默认实现
 */
public class NumberExpressionInstanceImpl implements NumberExpressionInstance {

	private final static BigInteger MAX_INT_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
	private final static BigInteger MIN_INT_VALUE = BigInteger.valueOf(Integer.MIN_VALUE);

	private final static BigInteger MAX_LONG_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
	private final static BigInteger MIN_LONG_VALUE = BigInteger.valueOf(Long.MIN_VALUE);

	private Class<? extends Number> type;

	private String originValue;

	private Object value;

	public NumberExpressionInstanceImpl(String value, boolean positive) throws RuleClassException {
		if (value.endsWith("f")) {
			type = Float.class;
			Float fv = Float.parseFloat(value.substring(0, value.length() - 1));
			this.value = positive ? fv : -fv;
		} else if (value.endsWith("d")) {
			type = Double.class;
			double dv = Double.parseDouble(value.substring(0, value.length() - 1));
			this.value = positive ? dv : -dv;

		} else if (value.endsWith("b")) {
			type = BigDecimal.class;
			BigDecimal bv = new BigDecimal(value.substring(0, value.length() - 1));
			this.value = positive ? bv : bv.negate();
		} else {
			BigInteger bigInteger = new BigInteger(value);

			if (bigInteger.compareTo(MAX_INT_VALUE) <= 0
							&& bigInteger.compareTo(MIN_INT_VALUE) >= 0) {
				this.type = Integer.class;
				int iv = Integer.parseInt(value);
				this.value = positive ? iv : -iv;

			} else if (bigInteger.compareTo(MAX_LONG_VALUE) <= 0
							&& bigInteger.compareTo(MIN_LONG_VALUE) >= 0) {

				this.type = Long.class;
				long lv = Long.parseLong(value);
				this.value = positive ? lv : -lv;

			} else {
				throw RuleClassException.syntaxException("数字超过已知范围");
			}
		}
		this.originValue = this.value.toString();
	}

	@Override
	public Object getObject(RuleInstance context) throws RuleInstanceException {
		return type.cast(value);
	}

	@Override
	public Class<? extends Number> getType() {
		return type;
	}

	@Override
	public String getExpressionString() {
		return originValue;
	}

}
