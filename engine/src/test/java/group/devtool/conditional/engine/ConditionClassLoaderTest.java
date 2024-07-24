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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.ConditionClassLoader;
import group.devtool.conditional.engine.Tree.Node;
import group.devtool.conditional.engine.Tree.ConstantNode;
import group.devtool.conditional.engine.Tree.JoinNode;
import group.devtool.conditional.engine.Tree.VariableNode;

public class ConditionClassLoaderTest {

	public List<Character> cs = new ArrayList<>();

	public String success = "";

	public String success2 = "";

	public List<Character> cs2 = new ArrayList<>();


	public List<Character> chs = new ArrayList<>();

	public String chSuccess = "";

	@Before
	public void read() {
		success += "IF";
		success += "  user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000\n";
		success += "THEN\n";
		success += "  SET(score.score, 100)\n";
		success += "END";
		for (int i = 0; i < success.length(); i++) {
			cs.add(success.charAt(i));
		}

		chSuccess += "IF";
		chSuccess += "  user.id == order.userId && (order.amount > 100 && order.amount < 500) && dayScore < 100 * 100\n";
		chSuccess += "THEN\n";
		chSuccess += "  SET(score.score, 100)\n";
		chSuccess += "END";
		for (int i = 0; i < chSuccess.length(); i++) {
			chs.add(chSuccess.charAt(i));
		}

		success2 += "IF";
		success2 += "  user.id == \"123\" && order.amount > 100 && order.amount < 500 && dayScore < 10000\n";
		success2 += "THEN\n";
		success2 += "  SET(score.score, 100)\n";
		success2 += "END";
		for (int i = 0; i < success2.length(); i++) {
			cs2.add(success2.charAt(i));
		}
	}

	@Test
	public void loadSimple() {
		ConditionClassLoader loader = new ConditionClassLoader();
		CacheRuleClass ruleClass = new CacheRuleClass("id");
		try {
			loader.load(0, cs, ruleClass);

			ConditionClassGroup group = ruleClass.getConditionGroup();
			assertEquals(1, group.getConditions().size());
			assertTrue(group instanceof CacheConditionClassGroup);

			Tree tree = ((CacheConditionClassGroup) group).getTree();
			Node node = tree.getRoot();
			List<Node> child = node.getChild();
			assertEquals(7, child.size());

			Node node100 = tree.get(new ConstantNode("100"));
			assertNotNull(node100);

			// 检查node100Child的子节点是否为 order.amount > 100
			List<Node> node100Child = node100.getChild();
			assertEquals(1, node100Child.size());

			Node amountGe100 = node100Child.get(0);
			assertEquals("order.amount>100", amountGe100.getKey());
			assertEquals(1, amountGe100.getChild().size());

			Node amountChild = amountGe100.getChild().get(0);
			assertEquals("order.amount>100&&order.amount<500&&dayScore<10000", amountChild.getKey());

			assertEquals(1, amountChild.getChild().size());
			Node fnd = amountChild.getChild().get(0);

			assertEquals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000", fnd.getKey());


			Node amount = tree.get(new VariableNode("order.amount"));
			assertEquals(2, amount.getChild().size());

			assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100")));
			assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount<500")));

			Node amountLt500 = amount.getChild().stream().filter(i -> i.getKey().equals("order.amount<500")).findAny().orElse(null);
			assertNotNull(amountLt500);
			assertEquals(1, amountLt500.getChild().size());

			Node amountDayScore = amountLt500.getChild().get(0);
			assertEquals("order.amount<500&&dayScore<10000", amountDayScore.getKey());
			assertEquals(amountChild.getKey(), amountDayScore.getChild().get(0).getKey());

			Node dayScore = tree.get(new VariableNode("dayScore"));
			assertEquals(1, dayScore.getChild().size());
			Node dayScoreChild = dayScore.getChild().get(0);
			assertEquals("dayScore<10000", dayScoreChild.getKey());

			Node join = tree.get(new JoinNode("user.id==order.userId"));
			assertNotNull(join);
			assertEquals(1, join.getChild().size());
			assertEquals(fnd.getKey(), join.getChild().get(0).getKey());

			Node uid = tree.get(new VariableNode("user.id"));
			assertNotNull(uid);
			assertEquals(1, uid.getChild().size());
			assertEquals(join.getKey(), uid.getChild().get(0).getKey());


			Node oid = tree.get(new VariableNode("order.userId"));
			assertNotNull(oid);
			assertEquals(1, oid.getChild().size());
			assertEquals(join.getKey(), oid.getChild().get(0).getKey());


		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void loadChild() {
		ConditionClassLoader loader = new ConditionClassLoader();
		CacheRuleClass ruleClass = new CacheRuleClass("id");
		try {
			loader.load(0, chs, ruleClass);
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		ConditionClassGroup group = ruleClass.getConditionGroup();
		assertEquals(1, group.getConditions().size());
		assertTrue(group instanceof CacheConditionClassGroup);
		Tree tree = ((CacheConditionClassGroup) group).getTree();
		Node node = tree.getRoot();

		List<Node> child = node.getChild();
		assertEquals(6, child.size());

		Node node100 = tree.get(new ConstantNode("100"));
		assertNotNull(node100);

		// 检查node100Child的子节点是否为 order.amount > 100
		List<Node> const100 = node100.getChild();
		assertEquals(2, const100.size());

		Node amountGe100 = const100.get(0);
		assertEquals("order.amount>100", amountGe100.getKey());
		assertEquals(1, amountGe100.getChild().size());

		Node mul100 = const100.get(1);
		assertEquals(1, mul100.getChild().size());
		assertEquals("100*100", mul100.getKey());


		Node amountChild = amountGe100.getChild().get(0);
		assertEquals("order.amount>100&&order.amount<500", amountChild.getKey());

		assertEquals(1, amountChild.getChild().size());
		Node dayScoreChild = amountChild.getChild().get(0);

		assertEquals("(order.amount>100&&order.amount<500)&&dayScore<100*100", dayScoreChild.getKey());


		Node amount = tree.get(new VariableNode("order.amount"));
		assertEquals(2, amount.getChild().size());

		assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100")));
		assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount<500")));

		Node amountLt500 = amount.getChild().stream().filter(i -> i.getKey().equals("order.amount<500")).findAny().orElse(null);
		assertNotNull(amountLt500);
		assertEquals(1, amountLt500.getChild().size());

		Node amountDayScore = amountLt500.getChild().get(0);
		assertEquals("order.amount>100&&order.amount<500", amountDayScore.getKey());
		assertEquals("(order.amount>100&&order.amount<500)&&dayScore<100*100", amountDayScore.getChild().get(0).getKey());


		Node dayScore = tree.get(new VariableNode("dayScore"));
		assertEquals(1, dayScore.getChild().size());
		Node dayScoreChild2 = dayScore.getChild().get(0);
		assertEquals("dayScore<100*100", dayScoreChild2.getKey());

		Node join2 = dayScoreChild2.getChild().get(0);
		assertEquals("(order.amount>100&&order.amount<500)&&dayScore<100*100", join2.getKey());


		Node join = tree.get(new JoinNode("user.id==order.userId"));
		assertNotNull(join);
		assertEquals(1, join.getChild().size());
		assertEquals("user.id==order.userId&&(order.amount>100&&order.amount<500)&&dayScore<100*100", join.getChild().get(0).getKey());

		Node uid = tree.get(new VariableNode("user.id"));
		assertNotNull(uid);
		assertEquals(1, uid.getChild().size());
		assertEquals(join.getKey(), uid.getChild().get(0).getKey());


		Node oid = tree.get(new VariableNode("order.userId"));
		assertNotNull(oid);
		assertEquals(1, oid.getChild().size());
		assertEquals(join.getKey(), oid.getChild().get(0).getKey());


	}

	@Test
	public void testMerge() {
		try {
			ConditionClassLoader loader1 = new ConditionClassLoader();
			CacheRuleClass ruleClass1 = new CacheRuleClass("id");
			loader1.load(0, cs, ruleClass1);
			CacheConditionClassGroup group1 = (CacheConditionClassGroup) ruleClass1.getConditionGroup();

			ConditionClassLoader loader2 = new ConditionClassLoader();
			CacheRuleClass ruleClass2 = new CacheRuleClass("id");
			loader2.load(0, chs, ruleClass2);
			CacheConditionClassGroup group2 = (CacheConditionClassGroup) ruleClass2.getConditionGroup();

			group1.addCondition(group2.getConditions().get(0));

			Tree tree = group1.getTree();
			Node node = tree.getRoot();

			List<Node> child = node.getChild();
			assertEquals(7, child.size());

			Node uid = tree.get(new VariableNode("user.id"));
			assertNotNull(uid);
			assertEquals(1, uid.getChild().size());
			assertEquals("user.id==order.userId", uid.getChild().get(0).getKey());


			Node oid = tree.get(new VariableNode("order.userId"));
			assertNotNull(oid);
			assertEquals(1, oid.getChild().size());
			assertEquals("user.id==order.userId", oid.getChild().get(0).getKey());

			Node fid = oid.getChild().get(0);
			assertEquals(2, fid.getChild().size());
			assertTrue(fid.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000")));
			assertTrue(fid.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==order.userId&&(order.amount>100&&order.amount<500)&&dayScore<100*100")));

			Node amount = tree.get(new VariableNode("order.amount"));
			assertNotNull(amount);
			assertEquals(2, amount.getChild().size());
			assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100")));
			assertTrue(amount.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount<500")));

			Node amount100 = tree.get(new JoinNode("order.amount>100"));
			assertNotNull(amount100);
			assertEquals(2, amount100.getChild().size());
			assertTrue(amount100.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100&&order.amount<500&&dayScore<10000")));
			assertTrue(amount100.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100&&order.amount<500")));


			Node n100 = tree.get(new ConstantNode("100"));
			assertNotNull(n100);
			assertEquals(2, n100.getChild().size());
			assertTrue(n100.getChild().stream().anyMatch(i -> i.getKey().equals("order.amount>100")));
			assertTrue(n100.getChild().stream().anyMatch(i -> i.getKey().equals("100*100")));

		} catch (RuleClassException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void testMerge2() {
		try {
			ConditionClassLoader loader1 = new ConditionClassLoader();
			CacheRuleClass ruleClass1 = new CacheRuleClass("id");
			loader1.load(0, cs, ruleClass1);
			CacheConditionClassGroup group1 = (CacheConditionClassGroup) ruleClass1.getConditionGroup();

			ConditionClassLoader loader2 = new ConditionClassLoader();
			CacheRuleClass ruleClass2 = new CacheRuleClass("id2");
			loader2.load(0, cs2, ruleClass2);
			CacheConditionClassGroup group2 = (CacheConditionClassGroup) ruleClass2.getConditionGroup();

			group1.addCondition(group2.getConditions().get(0));

			Tree tree = group1.getTree();
			Node node = tree.getRoot();

			List<Node> child = node.getChild();
			assertEquals(8, child.size());

			Node uid = tree.get(new VariableNode("user.id"));
			assertNotNull(uid);
			assertEquals(2, uid.getChild().size());
			assertTrue(uid.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==order.userId")));
			assertTrue(uid.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==\"123\"")));


			Node oid = tree.get(new VariableNode("order.userId"));
			assertNotNull(oid);
			assertEquals(1, oid.getChild().size());
			assertEquals("user.id==order.userId", oid.getChild().get(0).getKey());

			Node dayScore = tree.get(new VariableNode("dayScore"));
			assertNotNull(dayScore);
			assertEquals(1, dayScore.getChild().size());
			assertEquals("dayScore<10000", dayScore.getChild().get(0).getKey());

			Node dayScore100 = tree.get(new VariableNode("dayScore<10000"));
			assertNotNull(dayScore100);
			assertEquals(1, dayScore100.getChild().size());
			assertEquals("order.amount<500&&dayScore<10000", dayScore100.getChild().get(0).getKey());

			Node amountDayScore100 = tree.get(new VariableNode("order.amount<500&&dayScore<10000"));
			assertNotNull(amountDayScore100);
			assertEquals(1, amountDayScore100.getChild().size());
			assertEquals("order.amount>100&&order.amount<500&&dayScore<10000", amountDayScore100.getChild().get(0).getKey());

			Node amountDayScore100Child = tree.get(new VariableNode("order.amount>100&&order.amount<500&&dayScore<10000"));
			assertNotNull(amountDayScore100Child);
			assertEquals(2, amountDayScore100Child.getChild().size());
			assertTrue(amountDayScore100Child.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==\"123\"&&order.amount>100&&order.amount<500&&dayScore<10000")));
			assertTrue(amountDayScore100Child.getChild().stream().anyMatch(i -> i.getKey().equals("user.id==order.userId&&order.amount>100&&order.amount<500&&dayScore<10000")));


		} catch (RuleClassException e) {
			throw new RuntimeException(e);
		}
	}
}
