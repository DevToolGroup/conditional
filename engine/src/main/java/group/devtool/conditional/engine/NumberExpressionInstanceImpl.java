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

  public NumberExpressionInstanceImpl(String value) throws RuleClassException {
    this.originValue = value;

    if (value.endsWith("f")) {
      type = Float.class;
      this.value = Float.parseFloat(value.substring(0, value.length() - 1));
    } else if (value.endsWith("d")) {
      type = Double.class;
      this.value = Double.parseDouble(value.substring(0, value.length() - 1));

    } else if (value.endsWith("b")) {
      type = BigDecimal.class;
      this.value = new BigDecimal(value.substring(0, value.length() - 1));
    } else {
      BigInteger bigInteger = new BigInteger(value);

      if (bigInteger.compareTo(MAX_INT_VALUE) <= 0
          && bigInteger.compareTo(MIN_INT_VALUE) >= 0) {
        this.type = Integer.class;
        this.value = Integer.parseInt(value);

      } else if (bigInteger.compareTo(MAX_LONG_VALUE) <= 0
          && bigInteger.compareTo(MIN_LONG_VALUE) >= 0) {

        this.type = Long.class;
        this.value = Long.parseLong(value);

      } else {
        throw RuleClassException.syntaxException("数字超过已知范围");
      }
    }
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
