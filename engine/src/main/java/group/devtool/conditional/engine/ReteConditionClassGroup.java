package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import group.devtool.conditional.engine.Rete.ReteNode;
import group.devtool.conditional.engine.Rete.BetaNode;
import group.devtool.conditional.engine.Rete.RootNode;
import group.devtool.conditional.engine.Rete.TerminateNode;
import group.devtool.conditional.engine.Rete.AlphaNode;

/**
 * 基于Rete算法的规则处理
 */
public class ReteConditionClassGroup implements ConditionClassGroup {

  private Rete rete;

  private List<ConditionClass> conditions;

  private Integer order = 0;

  public ReteConditionClassGroup() {
    rete = new Rete();
    conditions = new ArrayList<>();
  }

  @Override
  public void addCondition(ConditionClass conditionClass) {
    conditionClass.setOrder(order);
    conditions.add(conditionClass);
    order += 1;
    ReteNode terminate = buildReteExpression(conditionClass);
    merge(terminate);
  }

  private void merge(ReteNode terminate) {
    List<ReteNode> nodes = new ArrayList<>();
    nodes.add(terminate);

    do {
      List<ReteNode> next = new ArrayList<>();
      for (ReteNode node : nodes) {
        if (rete.contains(node)) {
          // 可能子节点不存在
          if (node instanceof BetaNode) {
            BetaNode an = (BetaNode) node;
            next.add(an.getLeft());
            next.add(an.getRight());
          }
        } else if (node instanceof BetaNode) {
          BetaNode an = (BetaNode) node;
          if (rete.contains(an.getLeft())) {
            rete.get(an.getLeft()).addChild(Collections.singletonList(an));
          } else {
            next.add(an.getLeft());
          }
          if (rete.contains(an.getRight())) {
            rete.get(an.getRight()).addChild(Collections.singletonList(an));
          } else {
            next.add(an.getRight());
          }
        } else if (node instanceof TerminateNode) {
          TerminateNode tn = (TerminateNode) node;
          if (rete.contains(tn.getParent())) {
            rete.get(tn.getParent()).addChild(Collections.singletonList(tn));
          } else {
            next.add(tn.getParent());
          }
        } else if (node instanceof AlphaNode) {
          rete.getRoot().addChild(Collections.singletonList(node));
        } else {
          // do nothing
        }
      }
      nodes = next;

    } while (!nodes.isEmpty());
  }

  private ReteNode buildReteExpression(ConditionClass conditionClass) {
    Rete nr = new Rete();
    RootNode root = new RootNode(nr);
    TerminateNode terminate = new TerminateNode(nr, conditionClass);
    ReteNode expr = build(nr, root, conditionClass.getCondition().getInstance(), terminate);
    terminate.setParent(expr);
    return terminate;
  }

  private ReteNode build(Rete rete, RootNode root, ExpressionInstance expression, ReteNode child) {
    if (expression instanceof ChildExpressionInstance) {
      ChildExpressionInstance childInstance = (ChildExpressionInstance) expression;
      return build(rete, root, childInstance.getChild(), child);
    }

    if (expression instanceof LogicExpressionInstance) {
      LogicExpressionInstance logic = (LogicExpressionInstance) expression;
      BetaNode beta = new BetaNode(rete, logic);
      beta.addChild(Collections.singletonList(child));
      if (null != logic.left()) {
        beta.setLeft(build(rete, root, logic.left(), beta));
      }
      if (null != logic.right()) {
        beta.setRight(build(rete, root, logic.right(), beta));
      }
      return beta;
    }

    AlphaNode value = new AlphaNode(rete, expression);
    if (rete.contains(value)) {
      rete.get(value).addChild(Collections.singletonList(child));
    } else {
      value.addChild(Collections.singletonList(child));
      value.setParent(root);
      root.addChild(Collections.singletonList(value));
    }
    return value;

  }

  @Override
  public void invoke(RuleInstance instance) throws RuleInstanceException {
    rete.run(instance);
  }

  @Override
  public void completed() {
    rete.clear();
  }

  @Override
  public List<ConditionClass> getConditions() {
    return conditions;
  }

  public Rete getRete() {
    return rete;
  }

}
