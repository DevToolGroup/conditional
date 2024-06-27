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
    if (operation == Compare.GT) {
      return gt(context);
    }
    if (operation == Compare.GE) {
      return ge(context);
    }
    if (operation == Compare.LT) {
      return lt(context);
    }
    if (operation == Compare.LE) {
      return le(context);
    }
    if (operation == Compare.EQ) {
      return eq(context);
    }
    if (operation == Compare.NE) {
      return ne(context);
    }
    throw RuleInstanceException.runtimeException("不支持比较操作符");
  }

  private Object ne(RuleInstance context) throws RuleInstanceException {
    Object lv = left.getCacheObject(context);
    Object rv = right.getCacheObject(context);
    if (lv instanceof String || rv instanceof String) {
      if (lv == null) {
        return true;
      }
      return !lv.equals(rv);
    }
    return left.getCacheObject(context) != right.getCacheObject(context);
  }

  private Object eq(RuleInstance context) throws RuleInstanceException {
    Object lv = left.getCacheObject(context);
    Object rv = right.getCacheObject(context);
    if (lv instanceof String || rv instanceof String) {
      if (lv == null) {
        return false;
      }
      return lv.equals(rv);
    }
    return left.getCacheObject(context) == right.getCacheObject(context);
  }

  private boolean le(RuleInstance context) throws RuleInstanceException {
    return compare(context) <= 0;
  }

  private Object lt(RuleInstance context) throws RuleInstanceException {
    return compare(context) < 0;
  }

  private Object ge(RuleInstance context) throws RuleInstanceException {
    return compare(context) >= 0;
  }

  private Object gt(RuleInstance context) throws RuleInstanceException {
    return compare(context) > 0;
  }

  @SuppressWarnings("unchecked")
  private int compare(RuleInstance context) throws RuleInstanceException {
    Object lv = left.getCacheObject(context);
    Object rv = right.getCacheObject(context);
    if (lv instanceof Number && rv instanceof Number) {
      if (lv instanceof BigDecimal || rv instanceof BigDecimal) {
        BigDecimal leftBigDecimal = (BigDecimal) lv;
        BigDecimal rightBigDecimal = (BigDecimal) rv;
        return leftBigDecimal.compareTo(rightBigDecimal);
      } else if (lv instanceof Double || rv instanceof Double) {
        return Double.compare(((Double) lv).doubleValue(), ((Double) rv).doubleValue());
      } else if (lv instanceof Float || rv instanceof Float) {
        return Float.compare(((Float) lv).floatValue(), ((Float) rv).floatValue());
      } else if (lv instanceof Long || rv instanceof Long) {
        return Long.compare(((Long) lv).longValue(), ((Long) rv).longValue());
      } else if (lv instanceof Integer || rv instanceof Integer) {
        return Integer.compare(((Integer) lv).intValue(), ((Integer) rv).intValue());
      } else if (lv instanceof Short || rv instanceof Short) {
        return Short.compare(((Short) lv).shortValue(), ((Short) rv).shortValue());
      }
    }
    if (lv instanceof Comparable && rv instanceof Comparable) {
      return ((Comparable<Object>) lv).compareTo(rv);
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
    return left.getExpressionString() + operation.name() + right.getExpressionString();
  }

}
