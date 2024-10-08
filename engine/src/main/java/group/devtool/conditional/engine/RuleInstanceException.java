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

/**
 * 规则实例相关异常
 */
public class RuleInstanceException extends Exception {

  public RuleInstanceException(String message) {
    super(message);
  }

  public static RuleInstanceRuntimeException runtimeException(String message) {
    return new RuleInstanceRuntimeException(message);
  }

  public static UnExpectedRuleInstanceException unexpectedException(String message) {
    return new UnExpectedRuleInstanceException(message);
  }

  public static ValueResolveRuleInstanceException valueResolveException(String message) {
    return new ValueResolveRuleInstanceException(message);
  }

  public static RuleInstanceAlreadyInitException initException(String message) {
    return new RuleInstanceAlreadyInitException(message);
  }

  public static RuleInstanceInitParameterException parameterException(String message) {
    return new RuleInstanceInitParameterException(message);
  }

  public static RuleInstanceReturnResultException returnException(String message) {
    return new RuleInstanceReturnResultException(message);
  }

  public static RuleInstanceFunctionException functionException(String message) {
    return new RuleInstanceFunctionException(message);
  }

  /**
   * 不符合预期的规则实例异常
   */
  public static class UnExpectedRuleInstanceException extends RuleInstanceException {

    public UnExpectedRuleInstanceException(String message) {
      super(message);
    }

  }

  /**
   * 规则实例运行异常
   */
  public static class RuleInstanceRuntimeException extends RuntimeException {

    public RuleInstanceRuntimeException(String message) {
      super(message);
    }

  }

  /**
   * 变量解析异常
   */
  public static class ValueResolveRuleInstanceException extends RuntimeException {

    public ValueResolveRuleInstanceException(String message) {
      super(message);
    }

  }

  /**
   * 实例已初始化
   */
  public static class RuleInstanceAlreadyInitException extends RuntimeException {

    public RuleInstanceAlreadyInitException(String message) {
      super(message);
    }

  }

  public static class RuleInstanceInitParameterException extends RuleInstanceException {

    public RuleInstanceInitParameterException(String message) {
      super(message);
    }

  }

  public static class RuleInstanceReturnResultException extends RuleInstanceException {

    public RuleInstanceReturnResultException(String message) {
      super(message);
    }

  }

  public static class RuleInstanceFunctionException extends RuleInstanceException {

    public RuleInstanceFunctionException(String message) {
      super(message);
    }

  }

}
