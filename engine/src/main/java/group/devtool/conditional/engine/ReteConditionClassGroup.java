package group.devtool.conditional.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import group.devtool.conditional.engine.ReteNode.AlphaNode;
import group.devtool.conditional.engine.ReteNode.RootNode;
import group.devtool.conditional.engine.ReteNode.TerminateNode;
import group.devtool.conditional.engine.ReteNode.ValueNode;

/**
 * 基于Rete算法的规则处理
 */
public class ReteConditionClassGroup implements ConditionClassGroup {

  private RootNode root;

  private List<ConditionClass> conditions;

  public ReteConditionClassGroup() {
    root = new RootNode();
    conditions = new ArrayList<>();
  }

  @Override
  public void addCondition(ConditionClass conditionClass) {
    conditions.add(conditionClass);
    ReteNode terminate = buildReteExpression(conditionClass);
    merge(terminate);
  }

  private void merge(ReteNode terminate) {
    List<ReteNode> nodes = new ArrayList<>();
    nodes.add(terminate);

    boolean loop = true;
    do {
      List<ReteNode> next = new ArrayList<>();
      for (ReteNode node : nodes) {

        if (root.contains(node)) {
          continue;
        }

        if (node instanceof AlphaNode) {
          AlphaNode an = (AlphaNode) node;

          if (root.contains(an.getLeft())) {
            root.get(an.getLeft()).addChild(Collections.singletonList(an));
          } else {
            next.add(an.getLeft());
          }
          if (root.contains(an.getRight())) {
            root.get(an.getRight()).addChild(Collections.singletonList(an));
          } else {
            next.add(an.getRight());
          }

        } else if (node instanceof TerminateNode) {

          TerminateNode tn = (TerminateNode) node;
          if (root.contains(tn.getParent())) {
            root.get(tn.getParent()).addChild(Collections.singletonList(tn));
          } else {
            next.add(tn.getParent());
          }

        } else if (node instanceof ValueNode) {
          root.addChild(Collections.singletonList(node));

        } else {
          // do nothing
        }
      }
      nodes = next;

    } while (loop);
  }

  private ReteNode buildReteExpression(ConditionClass conditionClass) {
    ReteNode root = new RootNode();
    TerminateNode terminate = new TerminateNode(conditionClass);
    ReteNode expr = build(root, conditionClass.getCondition().getInstance(), terminate);
    terminate.setParent(expr);
    return terminate;
  }

  private ReteNode build(ReteNode root, ExpressionInstance expression, ReteNode child) {
    if (expression instanceof ComposeExpressionInstance) {
      ComposeExpressionInstance logic = (ComposeExpressionInstance) expression;
      AlphaNode alpha = new AlphaNode(logic);
      alpha.addChild(Collections.singletonList(child));
      alpha.setLeft(build(root, logic.left(), alpha));
      alpha.setRight(build(root, logic.right(), alpha));
      return alpha;

    } else {
      ValueNode value = new ValueNode(expression);
      value.addChild(Collections.singletonList(child));
      value.setParent(root);
      root.addChild(Collections.singletonList(value));
      return value;
    }
  }

  @Override
  public void invoke(RuleInstance instance) throws RuleInstanceException {
    List<TerminateNode> matched = new ArrayList<>();

    // 执行规则条件
    List<ValueNode> child = root.getValues();
    for (ValueNode node : child) {
      matched.addAll(node.invoke(instance));
    }

    // 执行规则动作
    for (TerminateNode terminate : matched) {
      List<ExpressionClass> actions = terminate.getActions();
      for (ExpressionClass actionClass: actions) {
        actionClass.getInstance().getObject(instance);
      }
    }

  }

  @Override
  public void completed() {
    root.clear();;
  }

  @Override
  public List<ConditionClass> getConditions() {
    return conditions;
  }

}
