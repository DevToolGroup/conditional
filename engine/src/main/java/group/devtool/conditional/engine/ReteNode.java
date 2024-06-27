package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ReteNode {

  private List<ReteNode> child;

  protected abstract String getKey();

  public List<ReteNode> getChild() {
    return child;
  }

  public void addChild(List<ReteNode> nodes) {
    child.addAll(nodes);
  }

  public List<TerminateNode> invoke(RuleInstance context) throws RuleInstanceException {
    List<TerminateNode> result = new ArrayList<>();

    Object value = doInvoke(context);
    for (ReteNode node : child) {
      if (node instanceof TerminateNode) {
        result.add((TerminateNode) node);
      } else {
        result.addAll(node.invoke(context));
      }
    }
    context.cacheExpressionValue(getKey(), value);
    return result;
  }

  protected abstract Object doInvoke(RuleInstance context) throws RuleInstanceException;

  public static class RootNode extends ReteNode {

    private Map<String, ReteNode> cache;

    public RootNode() {
      this.cache = new HashMap<>();
    }

    public boolean contains(ReteNode node) {
      if (node instanceof RootNode) {
        return true;
      }
      if (cache.containsKey(node.getKey())) {
        return true;
      }
      List<ReteNode> nodes = getChild();
      do {
        List<ReteNode> next = new ArrayList<>();

        for (ReteNode cn : nodes) {
          cache.put(cn.getKey(), cn);

          if (node.getKey().equals(cn.getKey())) {
            return true;
          }
          next.addAll(cn.getChild());
        }

        nodes = next;
      } while (!nodes.isEmpty());
      return false;
    }

    public ReteNode get(ReteNode node) {
      if (node instanceof RootNode) {
        return this;
      }
      return cache.get(node.getKey());
    }

    public void clear() {
      cache.clear();
    }

    public List<ValueNode> getValues() {
      List<ValueNode> result = new ArrayList<>();

      List<ReteNode> nodes = getChild();
      for (ReteNode node : nodes) {
        if (node instanceof ValueNode) {
          result.add((ValueNode) node);
        }
      }

      return result;
    }

    @Override
    protected String getKey() {
      return "root";
    }

    @Override
    protected Object doInvoke(RuleInstance context) {
      return null;
    }

  }

  public static class TerminateNode extends ReteNode {

    private ConditionClass conditionClass;

    private ReteNode parent;

    public TerminateNode(ConditionClass conditionClass) {
      this.conditionClass = conditionClass;
    }

    public ReteNode getParent() {
      return parent;
    }

    public void setParent(ReteNode parent) {
      this.parent = parent;
    }

    public List<ExpressionClass> getActions() {
      return conditionClass.getFunctions();
    }

    @Override
    protected String getKey() {
      return conditionClass.toString();
    }

    @Override
    protected Object doInvoke(RuleInstance context) {
      return null;
    }

  }

  public static class AlphaNode extends ReteNode {

    private ReteNode right;

    private ReteNode left;

    private ComposeExpressionInstance expression;

    public AlphaNode(ComposeExpressionInstance expression) {
      this.expression = expression;
    }

    public ReteNode getLeft() {
      return left;
    }

    public ReteNode getRight() {
      return right;
    }

    public void setRight(ReteNode right) {
      this.right = right;
    }

    public void setLeft(ReteNode left) {
      this.left = left;
    }

    public ComposeExpressionInstance getExpression() {
      return expression;
    }

    @Override
    protected String getKey() {
      return expression.getExpressionString();
    }

    @Override
    protected Object doInvoke(RuleInstance context) throws RuleInstanceException {
      return expression.getObject(context);
    }
  }

  public static class ValueNode extends ReteNode {

    private ExpressionInstance expression;

    private ReteNode parent;

    public ValueNode(ExpressionInstance expression) {
      this.expression = expression;
    }

    public ExpressionInstance getExpression() {
      return expression;
    }

    @Override
    protected String getKey() {
      return expression.getExpressionString();
    }

    public void setParent(ReteNode parent) {
      this.parent = parent;
    }

    public ReteNode getParent() {
      return parent;
    }

    @Override
    protected Object doInvoke(RuleInstance context) throws RuleInstanceException {
      return expression.getObject(context);
    }
  }

}
