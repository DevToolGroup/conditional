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

import java.util.*;
import java.util.stream.Collectors;

public class Tree {

	private final Map<String, Node> cache;

	private RootNode root;

	public Tree() {
		this.cache = new HashMap<>();
	}

	public boolean contains(Node node) {
		if (node instanceof RootNode) {
			return true;
		}
		if (cache.containsKey(node.getKey())) {
			return true;
		}
		return null != load(node);
	}

	private Node load(Node node) {
		List<Node> nodes = getRoot().getChild();
		do {
			List<Node> next = new ArrayList<>();
			for (Node cn : nodes) {
				// 加入缓存，优化后续查询
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

	public Node get(Node node) {
		if (node instanceof RootNode) {
			return root;
		}
		if (cache.containsKey(node.getKey())) {
			return cache.get(node.getKey());
		}
		return load(node);
	}

	public void add(Node... nodes) {
		for (Node node : nodes) {
			cache.put(node.getKey(), node);
		}
	}

	public void clear() {
		cache.clear();
	}

	public RootNode getRoot() {
		if (null == root) {
			root = new RootNode();
		}
		return root;
	}

	public void run(RuleInstance instance) throws RuleInstanceException {
		Memory memory = new Memory(instance);
		Agenda agenda = new Agenda();
		for (Node node : root.getChild()) {
			run(node, agenda, memory);
		}
		agenda.run(memory);
	}

	private void run(Node node, Agenda agenda, Memory memory) throws RuleInstanceException {
		if (node instanceof VariableNode) {
			VariableNode variable = (VariableNode) node;
			if (variable.support(memory)) {
				next(agenda, memory, variable);
			}

		} else if (node instanceof JoinNode) {
			JoinNode join = (JoinNode) node;
			if (join.active(memory)) {
				next(agenda, memory, join);
			}

		} else if (node instanceof TerminateNode) {
			agenda.add((TerminateNode) node);

		} else if (node instanceof ConstantNode) {
			next(agenda, memory, node);
		}
	}

	private void next(Agenda agenda, Memory memory, Node node)
					throws RuleInstanceException {
		for (Node child : node.getChild()) {
			if (child instanceof JoinNode) {
				JoinNode join = (JoinNode) child;
				ExpressionInstance expression = join.getExpression();
				Object value = expression.getCacheObject(memory.getInstance());
				if (expression instanceof LogicExpressionInstance || expression instanceof CompareExpressionInstance) {
					if (Boolean.TRUE.equals(value)) {
						memory.add(new Segment(child, node));
					}
				} else {
					memory.add(new Segment(child, node));
				}
			}
		}
		run(node.getChild(), agenda, memory);
	}

	private void run(List<Node> child, Agenda agenda, Memory memory) throws RuleInstanceException {
		for (Node node : child) {
			run(node, agenda, memory);
		}
	}

	public static class Segment {

		private final Node beta;

		private final Node prev;

		public Segment(Node beta, Node prev) {
			this.beta = beta;
			this.prev = prev;
		}

		public Node getBeta() {
			return beta;
		}

		public Node getPrev() {
			return prev;
		}

	}

	public static class Memory {

		private final RuleInstance instance;

		private final Map<String, List<String>> cache;

		public Memory(RuleInstance instance) {
			this.instance = instance;
			this.cache = new HashMap<>();
		}

		public boolean exist(JoinNode join, Node left) {
			if (cache.containsKey(join.getKey())) {
				List<String> prev = cache.get(join.getKey());
				return prev.contains(left.getKey());
			}
			return false;
		}

		public void add(Segment segment) {
			cache.computeIfAbsent(segment.getBeta().getKey(), k -> new ArrayList<>())
							.add(segment.getPrev().getKey());

		}

		public RuleInstance getInstance() {
			return instance;
		}

	}

	public static class Agenda {

		private final List<TerminateNode> terminates;

		public Agenda() {
			terminates = new ArrayList<>();
		}

		public void add(TerminateNode node) {
			this.terminates.add(node);
		}

		public void run(Memory memory) throws RuleInstanceException {
			if (terminates.isEmpty()) {
				return;
			}
			TerminateNode max = terminates.stream().max(Comparator.comparingInt(TerminateNode::getOrder)).get();
			for (ExpressionClass expression : max.getActions()) {
				expression.getInstance().getCacheObject(memory.getInstance());
			}
		}
	}

	public static abstract class Node {

		private final String key;

		private final List<Node> child;

		public Node(String key) {
			this.key = key;
			child = new ArrayList<>();
		}

		public String getKey() {
			return key;
		}

		public List<Node> getChild() {
			return child;
		}

		public void addChild(Node... nodes) {
			Set<String> childKeys = child.stream().map(Node::getKey).collect(Collectors.toSet());
			for (Node node : nodes) {
				if (!childKeys.contains(node.getKey())) {
					child.add(node);
				}
			}
		}

	}

	public static class RootNode extends Node {

		public RootNode() {
			super("root");
		}

	}

	public static class TerminateNode extends Node {

		private ConditionClass conditionClass;

		private Node parent;

		public TerminateNode(ConditionClass conditionClass) {
			super(conditionClass.toString());
			this.conditionClass = conditionClass;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public List<ExpressionClass> getActions() {
			return conditionClass.getFunctions();
		}

		public Integer getOrder() {
			return conditionClass.getOrder();
		}

	}

	public static class JoinNode extends Node {

		private ExpressionInstance expression;

		private List<Node> parent;

		public JoinNode(String key) {
			super(key);
			this.parent = new ArrayList<>();
		}

		public JoinNode(ExpressionInstance expression) {
			super(expression.getExpressionString());
			this.expression = expression;
			this.parent = new ArrayList<>();
		}

		public List<Node> getParent() {
			return parent;
		}

		public void addParent(Node... nodes) {
			parent.addAll(Arrays.asList(nodes));
		}

		public boolean active(Memory memory) {
			return parent.stream().allMatch(i -> memory.exist(this, i));
		}

		public ExpressionInstance getExpression() {
			return expression;
		}
	}

	public static class VariableNode extends Node {

		private ExpressionInstance expression;

		public VariableNode(String key) {
			super(key);
		}

		public VariableNode(ExpressionInstance expression) {
			super(expression.getExpressionString());
			this.expression = expression;
		}

		public boolean support(Memory memory) throws RuleInstanceException {
			return null != expression.getCacheObject(memory.getInstance());
		}
	}

	public static class ConstantNode extends Node {

		public ConstantNode(String key) {
			super(key);
		}

		public ConstantNode(ExpressionInstance expression) {
			super(expression.getExpressionString());
		}

	}

}
