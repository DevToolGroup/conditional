package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group.devtool.conditional.engine.Operation.Logic;

public class Rete {

  private Map<String, ReteNode> cache;

  private RootNode root;

  private boolean initialized = false;

  public Rete() {
    this.cache = new HashMap<>();
  }

  public boolean contains(ReteNode node) {
    if (node instanceof RootNode) {
      return true;
    }
    if (cache.containsKey(node.getKey())) {
      return true;
    }
    return null != load(node);
  }

  private ReteNode load(ReteNode node) {
    List<ReteNode> nodes = getRoot().getChild();
    do {
      List<ReteNode> next = new ArrayList<>();
      for (ReteNode cn : nodes) {
        cache.put(cn.getKey(), cn);
        if (node.getKey().equals(cn.getKey())) {
          return cn;
        }
        next.addAll(cn.getChild());
      }
      nodes = next;
    } while (!nodes.isEmpty());
    return null;
  }

  public ReteNode get(ReteNode node) {
    if (node instanceof RootNode) {
      return root;
    }
    if (initialized) {
      return load(node);
    }
    return cache.get(node.getKey());
  }

  public void add(List<ReteNode> nodes) {
    for (ReteNode node : nodes) {
      cache.put(node.getKey(), node);
    }
  }

  public void clear() {
    cache.clear();
    initialized = true;
  }

  public RootNode getRoot() {
    if (null == root) {
      root = new RootNode(this);
    }
    return root;
  }

  public void run(RuleInstance instance) throws RuleInstanceException {
    ReteMemory memory = new ReteMemory(instance);
    ReteAgenda agenda = new ReteAgenda();
    for (ReteNode node : root.getChild()) {
      run(node, agenda, memory);
    }
    agenda.run(memory);
  }

  private void run(ReteNode node, ReteAgenda agenda, ReteMemory memory) throws RuleInstanceException {
    if (node instanceof AlphaNode) {
      AlphaNode alpha = (AlphaNode) node;
      Object value = alpha.getExpression().getCacheObject(memory.getInstance());
      next(agenda, memory, alpha, value);

    } else if (node instanceof BetaNode) {
      BetaNode beta = (BetaNode) node;
      if (pass(beta, memory)) {
        next(agenda, memory, beta, true);
      }

    } else if (node instanceof TerminateNode) {
      agenda.add((TerminateNode) node);

    } else {
      // do nothing
    }
  }

  private void next(ReteAgenda agenda, ReteMemory memory, ReteNode node, Object value)
      throws RuleInstanceException {
    for (ReteNode child : node.getChild()) {
      if (child instanceof BetaNode) {
        memory.add(new BetaMemory(child, node, value));
      }
    }
    run(node.getChild(), agenda, memory);
  }

  private boolean pass(BetaNode beta, ReteMemory memory) throws RuleInstanceException {
    LogicExpressionInstance expr = beta.getExpression();
    if (expr.getLogic().equals(Logic.AND)) {
      if (!memory.exist(beta, beta.getLeft()) || !memory.exist(beta, beta.getRight())) {
        return false;
      }
      Object lv = memory.find(beta, beta.getLeft());
      Object rv = memory.find(beta, beta.getRight());
      return (Boolean) lv && (Boolean) rv;
    } else if (expr.getLogic().equals(Logic.OR)) {
      if (!memory.exist(beta, beta.getLeft()) || !memory.exist(beta, beta.getRight())) {
        return false;
      }
      Object lv = memory.find(beta, beta.getLeft());
      Object rv = memory.find(beta, beta.getRight());
      return (Boolean) lv || (Boolean) rv;
    } else if (expr.getLogic().equals(Logic.NOT)) {
      if (!memory.exist(beta, beta.getLeft())) {
        return false;
      }
      Object lv = memory.find(beta, beta.getLeft());
      return !((Boolean) lv);
    } else {
      return false;
    }
  }

  private void run(List<ReteNode> child, ReteAgenda agenda, ReteMemory memory) throws RuleInstanceException {
    for (ReteNode node : child) {
      run(node, agenda, memory);
    }
  }

  public static class BetaMemory {

    private ReteNode beta;

    private Object value;

    private ReteNode prev;

    public BetaMemory(ReteNode beta, ReteNode prev, Object value) {
      this.beta = beta;
      this.value = value;
      this.prev = prev;
    }

    public ReteNode getBeta() {
      return beta;
    }

    public Object getValue() {
      return value;
    }

    public ReteNode getPrev() {
      return prev;
    }

  }

  public static class ReteMemory {

    private RuleInstance instance;

    private Map<String, Map<String, Object>> cache;

    public ReteMemory(RuleInstance instance) {
      this.instance = instance;
      this.cache = new HashMap<>();
    }

    public boolean exist(BetaNode beta, ReteNode left) {
      if (cache.containsKey(beta.getKey())) {
        Map<String, Object> prev = cache.get(beta.getKey());
        return prev.containsKey(left.getKey());
      }
      return false;
    }

    public Object find(BetaNode beta, ReteNode left) {
      return cache.get(beta.getKey()).get(left.getKey());
    }

    public void add(BetaMemory memory) {
      cache.computeIfAbsent(memory.getBeta().getKey(), k -> new HashMap<>())
          .put(memory.getPrev().getKey(),memory.getValue());

    }

    public RuleInstance getInstance() {
      return instance;
    }

  }

  public static class ReteAgenda {

    private List<TerminateNode> terminates;

    public ReteAgenda() {
      terminates = new ArrayList<>();
    }

    public void add(TerminateNode node) {
      this.terminates.add(node);
    }

    public void run(ReteMemory memory) throws RuleInstanceException {
      if (terminates.isEmpty()) {
        return;
      }
      TerminateNode max = terminates.stream().max(Comparator.comparingInt(TerminateNode::getOrder)).get();
      for (ExpressionClass expression : max.getActions()) {
        expression.getInstance().getCacheObject(memory.getInstance());
      }
    }

  }

  public static abstract class ReteNode {

    private List<ReteNode> child;

    private Rete rete;

    public ReteNode(Rete rete) {
      child = new ArrayList<>();
      this.rete = rete;
    }

    protected abstract String getKey();

    public List<ReteNode> getChild() {
      return child;
    }

    public void addChild(List<ReteNode> nodes) {
      rete.add(nodes);
      child.addAll(nodes);
    }
  }

  public static class RootNode extends ReteNode {

    public RootNode(Rete rete) {
      super(rete);
    }

    @Override
    protected String getKey() {
      return "root";
    }

  }

  public static class TerminateNode extends ReteNode {

    private ConditionClass conditionClass;

    private ReteNode parent;

    private String key;

    public TerminateNode(Rete rete, String key) {
      super(rete);
      this.key = key;
    }

    public TerminateNode(Rete rete, ConditionClass conditionClass) {
      super(rete);
      this.conditionClass = conditionClass;
      this.key = conditionClass.toString();
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

    public Integer getOrder() {
      return conditionClass.getOrder();
    }

    @Override
    protected String getKey() {
      return key;
    }

  }

  public static class BetaNode extends ReteNode {

    private ReteNode right;

    private ReteNode left;

    private LogicExpressionInstance expression;

    private String key;

    public BetaNode(Rete rete, String key) {
      super(rete);
      this.key = key;
    }

    public BetaNode(Rete rete, LogicExpressionInstance expression) {
      super(rete);
      this.expression = expression;
      this.key = expression.getExpressionString();
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

    public LogicExpressionInstance getExpression() {
      return expression;
    }

    @Override
    protected String getKey() {
      return key;
    }

  }

  public static class AlphaNode extends ReteNode {

    private ExpressionInstance expression;

    private ReteNode parent;

    private String key;

    public AlphaNode(Rete rete, String key) {
      super(rete);
      this.key = key;
    }

    public AlphaNode(Rete rete, ExpressionInstance expression) {
      super(rete);
      this.expression = expression;
      this.key = expression.getExpressionString();
    }

    public ExpressionInstance getExpression() {
      return expression;
    }

    @Override
    protected String getKey() {
      return key;
    }

    public void setParent(ReteNode parent) {
      this.parent = parent;
    }

    public ReteNode getParent() {
      return parent;
    }
  }

}
