package group.devtool.conditional.documentation;

import group.devtool.conditional.engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunRuleEngineExample {

	public static void main(String[] args) throws RuleInstanceException, RuleClassException {
		// 初始化规则文件加载器
		RuleClassService ruleClassService = new ResourceRuleClassService("sendScore.rl");

		// 初始化规则引擎服务
		RuleInstanceServiceProvider provider = new RuleInstanceServiceProvider(ruleClassService);
		RuleInstanceService service = provider.get();

		// 执行规则引擎
		Map<String, Object> result1 = service.exec(send100());
		System.out.println("预期赠送积分数量：100，实际赠送积分数量：" + result1.get("score"));

		Map<String, Object> result2 = service.exec(send500());
		System.out.println("预期赠送积分数量：500，实际赠送积分数量：" + result2.get("score"));
	}

	public static Map<String, Object> send100() {
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

	public static Map<String, Object> send500() {
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
}
