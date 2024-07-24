package group.devtool.conditional.engine;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 基于resources文件的规则定义服务
 */
public class ResourceRuleClassService implements RuleClassService {

	private String dl;

	private final RuleClassLoader loader;

	public ResourceRuleClassService(RuleClassLoader classLoader) {
		this.loader = classLoader;
	}

	public ResourceRuleClassService(String ruleClassFile) throws RuleClassException {
		this(new CacheRuleClassLoader(ruleClassFile));
		InputStream inputStream = ResourceRuleClassService.class.getClassLoader().getResourceAsStream(ruleClassFile);
		StringBuilder builder = new StringBuilder();
		if (inputStream == null) {
			throw RuleClassException.ruleClassFileNotFound(ruleClassFile);
		}
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			while (scanner.hasNextLine()) {
				builder.append(scanner.nextLine());
				builder.append("\n");
			}
		}
		dl = builder.toString();
	}

	@Override
	public RuleClass loadRuleClass() throws RuleClassException {
		return loader.load(dl);
	}


}
