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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RuleInstanceServiceTest {

	public static String dl = "";

	@Before
	public void read() {
		dl += "TYPE History \"积分历史\"\n";
		dl += "Integer time \"时间\"\n";
		dl += "Integer count \"积分数量\"\n";
		dl += "END\n";

		dl += "TYPE User \"用户\"\n";
		dl += "Integer       id        \"用户ID\"\n";
		dl += "Boolean       isNew      \"是否新用户\"\n";
		dl += "List<History> histories \"积分历史\"\n";
		dl += "END\n";

		dl += "TYPE Order \"订单\"\n";
		dl += "Integer    userId       \"用户\"\n";
		dl += "Integer    amount     \"订单金额\"\n";
		dl += "END\n";

		dl += "TYPE Score \"积分\"\n";
		dl += "Integer    score      \"积分数量\"\n";
		dl += "List<History>    histories     \"积分记录\"\n";
		dl += "END\n";

		dl += "ARG Order order, User user\n";

		dl += "RETURN Score score\n";

		dl += "CONST List<History> dayHistory \"当日积分记录\" = FILTER(user.histories, \"time\", 1)\n";
		dl += "CONST Integer dayScore \"当日积分数量\" = SUM(RETRIEVE(dayHistory, \"count\"))\n";

		dl += "IF\n";
		dl += " user.isNew\n";
		dl += "THEN\n";
		dl += " PUT(score, \"score\", 1000)\n";
		dl += "END\n";

		dl += "IF\n";
		dl += " user.id == order.userId && order.amount > 500 && dayScore < 10000\n";
		dl += "THEN\n";
		dl += " PUT(score, \"score\", 500)\n";
		dl += "END\n";

		dl += "IF\n";
		dl += " user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000\n";
		dl += "THEN\n";
		dl += " PUT(score, \"score\", 100)\n";
		dl += "END\n";

		dl += "IF\n";
		dl += " user.id == order.userId && order.amount > 500 && dayScore < 10000\n";
		dl += "THEN\n";
		dl += " PUT(score, \"score\", 500)\n";
		dl += "END\n";
	}

	@Test
	public void invokeSend100Score() {
		RuleClassService ruleClassService = new TestCacheRuleClassService();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();

		try {
			result = instanceService.exec(send100Score());
			assertEquals(100, result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void invokeSend500Score() {
		RuleClassService ruleClassService = new TestCacheRuleClassService();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();
		try {
			result = instanceService.exec(send500Score());
			assertEquals(500, result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void invokeSendNullScore() {
		RuleClassService ruleClassService = new TestCacheRuleClassService();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();

		try {
			result = instanceService.exec(sendNullScore());
			assertNull(result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void invokeSendMoreDayScore() {
		RuleClassService ruleClassService = new TestCacheRuleClassService();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();

		try {
			result = instanceService.exec(sendMoreDayScore());
			assertNull(result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void invokeSend1000Score() {
		RuleClassService ruleClassService = new TestCacheRuleClassService();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();

		try {
			Map<String, Object> newUser = sendNewUser();
			result = instanceService.exec(newUser);
			assertEquals(1000, result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void invokeSend500ScoreNoCache() {
		RuleClassService ruleClassService = new TestRuleClassServiceImpl();
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService instanceService = provider.get();

		Map<String, Object> result = new HashMap<>();

		try {
			result = instanceService.exec(false, false, send500Score());
			assertEquals(500, result.get("score"));
		} catch (RuleInstanceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuleClassException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private Map<String, Object> sendNewUser() {
		Map<String, Object> result = new HashMap<>();

		Map<String, Object> user = new HashMap<>();
		user.put("id", 1);
		user.put("isNew", true);
		user.put("histories", new ArrayList<>());
		result.put("user", user);


		return result;
	}

	private Map<String, Object> sendMoreDayScore() {
		Map<String, Object> result = new HashMap<>();

		List<Map<String, Object>> histories = new ArrayList<>();
		Map<String, Object> h1 = new HashMap<>();
		h1.put("time", 1);
		h1.put("count", 5000);
		histories.add(h1);
		Map<String, Object> h2 = new HashMap<>();
		h2.put("time", 1);
		h2.put("count", 6000);
		histories.add(h2);

		Map<String, Object> user = new HashMap<>();
		user.put("id", 1);
		user.put("histories", histories);
		result.put("user", user);

		Map<String, Object> order = new HashMap<>();
		order.put("userId", 1);
		order.put("amount", 200);
		result.put("order", order);

		return result;
	}

	private Map<String, Object> sendNullScore() {
		Map<String, Object> result = new HashMap<>();

		List<Map<String, Object>> histories = new ArrayList<>();
		Map<String, Object> h1 = new HashMap<>();
		h1.put("time", 1);
		h1.put("count", 1000);
		histories.add(h1);
		Map<String, Object> h2 = new HashMap<>();
		h2.put("time", 2);
		h2.put("count", 10000);
		histories.add(h2);

		Map<String, Object> user = new HashMap<>();
		user.put("id", 1);
		user.put("histories", histories);
		result.put("user", user);

		Map<String, Object> order = new HashMap<>();
		order.put("userId", 1);
		order.put("amount", 50);
		result.put("order", order);

		return result;
	}

	private Map<String, Object> send500Score() {
		Map<String, Object> result = new HashMap<>();

		List<Map<String, Object>> histories = new ArrayList<>();
		Map<String, Object> h1 = new HashMap<>();
		h1.put("time", 1);
		h1.put("count", 1000);
		histories.add(h1);
		Map<String, Object> h2 = new HashMap<>();
		h2.put("time", 2);
		h2.put("count", 10000);
		histories.add(h2);

		Map<String, Object> user = new HashMap<>();
		user.put("id", 1);
		user.put("histories", histories);
		result.put("user", user);

		Map<String, Object> order = new HashMap<>();
		order.put("userId", 1);
		order.put("amount", 600);
		result.put("order", order);

		return result;
	}

	private Map<String, Object> send100Score() {
		Map<String, Object> result = new HashMap<>();

		List<Map<String, Object>> histories = new ArrayList<>();
		Map<String, Object> h1 = new HashMap<>();
		h1.put("time", 1);
		h1.put("count", 1000);
		histories.add(h1);
		Map<String, Object> h2 = new HashMap<>();
		h2.put("time", 2);
		h2.put("count", 10000);
		histories.add(h2);

		Map<String, Object> user = new HashMap<>();
		user.put("id", 1);
		user.put("histories", histories);
		result.put("user", user);

		Map<String, Object> order = new HashMap<>();
		order.put("userId", 1);
		order.put("amount", 200);
		result.put("order", order);

		return result;
	}

	public static class TestCacheRuleClassService implements RuleClassService {

		@Override
		public RuleClass loadRuleClass() throws RuleClassException {
			CacheRuleClassLoader loader = new CacheRuleClassLoader("id");
			return loader.load(dl);
		}

	}

	public static class TestRuleClassServiceImpl implements RuleClassService {

		@Override
		public RuleClass loadRuleClass() throws RuleClassException {
			RuleClassLoaderImpl loader = new RuleClassLoaderImpl("id");
			return loader.load(dl);
		}

	}

}
