package group.devtool.conditional.documentation;

import group.devtool.conditional.engine.ReteRuleClassLoader;
import group.devtool.conditional.engine.RuleClass;
import group.devtool.conditional.engine.RuleClassException;
import group.devtool.conditional.engine.RuleClassService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 基于resources文件的规则定义服务
 */
public class ExampleResourceRuleClassService implements RuleClassService {

	@Override
	public RuleClass loadRuleClass(String ruleClassFile) throws RuleClassException {
		InputStream inputStream = ExampleResourceRuleClassService.class.getClassLoader().getResourceAsStream(ruleClassFile);
		StringBuilder builder = new StringBuilder();
		if (inputStream == null) {
			throw RuleClassException.ruleClassFileNotFound(ruleClassFile);
		}
		Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
		while (scanner.hasNextLine()) {
			builder.append(scanner.nextLine());
			builder.append("\n");
		}
		scanner.close();
		ReteRuleClassLoader loader = new ReteRuleClassLoader(ruleClassFile, builder.toString());
		return loader.load();
	}


}
