package group.devtool.conditional.engine;

import java.math.BigDecimal;

import group.devtool.conditional.engine.Operation.Arith;

/**
 * {@link ArithExpressionInstance} 默认实现类
 */
public class ArithExpressionInstanceImpl implements ArithExpressionInstance {

  private ExpressionInstance left;

  private Arith arith;

  private ExpressionInstance right;

  public ArithExpressionInstanceImpl(ExpressionInstance left, Arith arith, ExpressionInstance right) {
    this.left = left;
    this.arith = arith;
    this.right = right;
  }

  @Override
  public Object getObject(RuleInstance context) throws RuleInstanceException {
    Object lv = left.getCacheObject(context);
    Object rv = right.getCacheObject(context);
    if (!(lv instanceof Number && rv instanceof Number)) {
      throw RuleInstanceException.unexpectedException("算术运算仅支持数字类型");
    }
    if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
      return arithDecimal((BigDecimal) lv, (BigDecimal) rv);
    } else if (lv instanceof Long && rv instanceof Long) {
      return arithLong((Long) lv, (Long) rv);
    } else if (lv instanceof Integer && rv instanceof Integer) {
      return arithInteger((Integer) lv, (Integer) rv);
    } else if (lv instanceof Float && rv instanceof Float) {
      return arithFloat((Float) lv, (Float) rv);
    } else if (lv instanceof Double && rv instanceof Double) {
      return arithDouble((Double) lv, (Double) rv);
    } else {
      throw RuleInstanceException.unexpectedException("当前数字类型不支持，式子两边类型："
          + lv.getClass().getSimpleName()
          + " , "
          + rv.getClass().getSimpleName());
    }
  }

  private Object arithDouble(Double lv, Double rv) {
    if (arith == Arith.PLUS) {
      return lv + rv;
    } else if (arith == Arith.MINUS) {
      return lv - rv;
    } else if (arith == Arith.MUL) {
      return lv * rv;
    } else if (arith == Arith.DIV) {
      return lv / rv;
    } else if (arith == Arith.MOD) {
      return lv % rv;
    } else {
      throw RuleInstanceException.runtimeException("不支持的运算");
    }
  }

  private Object arithFloat(Float lv, Float rv) {
    if (arith == Arith.PLUS) {
      return lv + rv;
    } else if (arith == Arith.MINUS) {
      return lv - rv;
    } else if (arith == Arith.MUL) {
      return lv * rv;
    } else if (arith == Arith.DIV) {
      return lv / rv;
    } else if (arith == Arith.MOD) {
      return lv % rv;
    } else {
      throw RuleInstanceException.runtimeException("不支持的运算");
    }
  }

  private Object arithInteger(Integer lv, Integer rv) {
    if (arith == Arith.PLUS) {
      return lv + rv;
    } else if (arith == Arith.MINUS) {
      return lv - rv;
    } else if (arith == Arith.MUL) {
      return lv * rv;
    } else if (arith == Arith.DIV) {
      return lv / rv;
    } else if (arith == Arith.MOD) {
      return lv % rv;
    } else {
      throw RuleInstanceException.runtimeException("不支持的运算");
    }
  }

  private Object arithLong(Long lv, Long rv) {
    if (arith == Arith.PLUS) {
      return lv + rv;
    } else if (arith == Arith.MINUS) {
      return lv - rv;
    } else if (arith == Arith.MUL) {
      return lv * rv;
    } else if (arith == Arith.DIV) {
      return lv / rv;
    } else if (arith == Arith.MOD) {
      return lv % rv;
    } else {
      throw RuleInstanceException.runtimeException("不支持的运算");
    }
  }

  private Object arithDecimal(BigDecimal lv, BigDecimal rv) {
    if (arith == Arith.PLUS) {
      return lv.add(rv);
    } else if (arith == Arith.MINUS) {
      return lv.subtract(rv);
    } else if (arith == Arith.MUL) {
      return lv.multiply(rv);
    } else if (arith == Arith.DIV) {
      return lv.divide(rv);
    } else if (arith == Arith.MOD) {
      return lv.remainder(rv);
    } else {
      throw RuleInstanceException.runtimeException("不支持的运算");
    }
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
    return left.getExpressionString() + arith.op() + right.getExpressionString();
  }

  @Override
  public Arith getArith() {
    return arith;
  }

}
