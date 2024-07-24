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

import java.util.ArrayList;
import java.util.List;

import group.devtool.conditional.engine.Tree.Node;
import group.devtool.conditional.engine.Tree.JoinNode;
import group.devtool.conditional.engine.Tree.ConstantNode;
import group.devtool.conditional.engine.Tree.RootNode;
import group.devtool.conditional.engine.Tree.TerminateNode;
import group.devtool.conditional.engine.Tree.VariableNode;

/**
 * 规则合并，实现节点共享
 */
public class CacheConditionClassGroup implements ConditionClassGroup {

	private final Tree tree;

	private final List<ConditionClass> conditions;

	private Integer order = 0;

	public CacheConditionClassGroup() {
		tree = new Tree();
		conditions = new ArrayList<>();
	}

	@Override
	public void addCondition(ConditionClass conditionClass) {
		conditionClass.setOrder(order);
		conditions.add(conditionClass);
		order += 1;

		Node terminate = buildTree(conditionClass);
		mergeTree(terminate);
	}

	private void mergeTree(Node terminate) {
		List<Node> nodes = new ArrayList<>();
		nodes.add(terminate);

		do {
			List<Node> next = new ArrayList<>();
			for (Node node : nodes) {
				if (node instanceof JoinNode) {
					JoinNode an = (JoinNode) node;

					List<Node> exists = new ArrayList<>();
					for (Node parent : an.getParent()) {
						if (!tree.contains(parent)) {
							next.add(parent);
						} else {
							exists.add(tree.get(parent));
						}
					}
					for (Node exist : exists) {
						exist.addChild(an);
					}

				} else if (node instanceof TerminateNode) {
					TerminateNode tn = (TerminateNode) node;
					if (tree.contains(tn.getParent())) {
						tree.get(tn.getParent()).addChild(tn);
					} else {
						next.add(tn.getParent());
					}
				} else if (node instanceof VariableNode) {
					if (!tree.contains(node)) {
						tree.getRoot().addChild(node);
					}

				} else if (node instanceof ConstantNode) {
					if (!tree.contains(node)) {
						tree.getRoot().addChild(node);
					}
				} else {
					// do nothing
				}
			}
			nodes = next;

		} while (!nodes.isEmpty());
	}

	private Node buildTree(ConditionClass conditionClass) {
		Tree nr = new Tree();

		RootNode root = new RootNode();
		nr.add(root);

		TerminateNode terminate = new TerminateNode(conditionClass);
		nr.add(terminate);

		Node expr = build(nr, conditionClass.getCondition().getInstance(), terminate);
		terminate.setParent(expr);
		return terminate;
	}

	private Node build(Tree tree, ExpressionInstance expression, Node child) {
		Node node;
		if (expression instanceof ChildExpressionInstance) {
			ChildExpressionInstance childInstance = (ChildExpressionInstance) expression;
			Node childNode = build(tree, childInstance.getChild(), child);
			if (tree.contains(childNode)) {
				node = tree.get(childNode);
			} else {
				tree.add(childNode);
				node = childNode;
			}
		} else if (expression instanceof ComposeExpressionInstance) {
			ComposeExpressionInstance compose = (ComposeExpressionInstance) expression;
			JoinNode join = new JoinNode(compose);
			if (tree.contains(join)) {
				node = tree.get(join);
			} else {
				join.addChild(child);
				if (null != compose.left()) {
					join.addParent(build(tree, compose.left(), join));
				}
				if (null != compose.right()) {
					join.addParent(build(tree, compose.right(), join));
				}
				tree.add(join);
				node = join;
			}
		} else if (expression instanceof FunctionExpressionInstance) {
			FunctionExpressionInstance func = (FunctionExpressionInstance) expression;
			JoinNode join = new JoinNode(func);
			if (tree.contains(join)) {
				node = tree.get(join);
			} else {
				join.addChild(child);
				for (ExpressionInstance arg : func.getArguments()) {
					join.addParent(build(tree, arg, join));
				}
				tree.add(join);
				node = join;
			}
		} else if (expression instanceof VariableExpressionInstance) {
			VariableNode variable = new VariableNode(expression);
			if (tree.contains(variable)) {
				node = tree.get(variable);
				node.addChild(child);
			} else {
				variable.addChild(child);
				tree.getRoot().addChild(variable);
				tree.add(variable);
				node = variable;
			}
		} else {
			Node value = new ConstantNode(expression);
			if (tree.contains(value)) {
				value = tree.get(value);
				value.addChild(child);
			} else {
				value.addChild(child);
				tree.getRoot().addChild(value);
				tree.add(value);
			}
			node = value;
		}
		return node;
	}

	@Override
	public void invoke(RuleInstance instance) throws RuleInstanceException {
		tree.run(instance);
	}

	@Override
	public void completed() {
		tree.clear();
	}

	@Override
	public List<ConditionClass> getConditions() {
		return conditions;
	}

	public Tree getTree() {
		return tree;
	}

}
