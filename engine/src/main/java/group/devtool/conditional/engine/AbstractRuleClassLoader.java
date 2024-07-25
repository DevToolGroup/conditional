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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import group.devtool.conditional.engine.Token.TypeToken;

/**
 * 基于规则文本的规则定义加载器
 */
public abstract class AbstractRuleClassLoader implements RuleClassLoader {

	public static final Character ASSIGN = '=';

	public static final Character LF = '\n';

	public static final Character COMMENT = '#';

	public static final Character BOUND = '"';

	public static final Character COMMA = ',';

	private final List<Character> characters = new ArrayList<>();

	private final RuleElementClassLoader loader;

	public AbstractRuleClassLoader() {
		this.loader = new RuleElementClassLoader();
	}

	@Override
	public RuleClass load(String dl) throws RuleClassException {
		for (int i = 0; i < dl.length(); i++) {
			characters.add(dl.charAt(i));
		}
		AbstractRuleClass ruleClass = buildRuleClass();
		loader.load(0, characters, ruleClass);
		return ruleClass;
	}

	/**
	 * 构造规则定义
	 *
	 * @return 规则定义
	 */
	protected abstract AbstractRuleClass buildRuleClass();

	public static boolean startWith(int pos, String pattern, List<Character> characters) {
		int offset = 0;
		int index = pos;
		while (offset < pattern.length()) {
			char character = (char) characters.get(index);
			if (character == pattern.charAt(offset)) {
				offset += 1;
			} else {
				return false;
			}
			index += 1;
		}
		return true;
	}

	public static int readUntil(int pos, CharSequence pattern, List<Character> characters) throws RuleClassException {
		return readUntil(pos, pattern, characters, false);
	}

	public static int readUntil(int pos, CharSequence pattern, List<Character> characters, boolean includeEnd)
					throws RuleClassException {
		int offset = 0;
		int index = pos;
		while (offset < pattern.length() && index < characters.size()) {
			Character character = characters.get(index);
			if (character == pattern.charAt(offset)) {
				offset += 1;
			} else {
				offset = 0;
			}
			index += 1;
		}
		if (offset < pattern.length()) {
			throw RuleClassException.syntaxException(index, pattern);
		}
		if (includeEnd) {
			return index;
		}
		return index - 1;
	}

	public static int readUntil(int pos, Function<Character, Boolean> eof, List<Character> characters)
					throws RuleClassException {
		int index = pos;
		while (index < characters.size()) {
			Character character = characters.get(index);
			if (eof.apply(character)) {
				return index;
			}
			index += 1;
		}
		return index;
	}

	/**
	 * 规则元素定义加载器
	 */
	public static class RuleElementClassLoader extends AbstractElementClassLoader {

		private List<ElementClassLoader> loaders = new ArrayList<>();

		public RuleElementClassLoader() {
			loaders.add(new FactClassLoader());
			loaders.add(new ArgumentClassLoader());
			loaders.add(new ReturnClassLoader());
			loaders.add(new VariableClassLoader());
			loaders.add(new ConditionClassLoader());
		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			return true;
		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			// 校验规则定义是否满足条件
			ruleClass.completed();
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return characters.size();
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) throws RuleClassException {
			return loaders.iterator();
		}

	}

	/**
	 * 事实定义加载器
	 */
	public static class FactClassLoader extends AbstractElementClassLoader {

		private static String DECLARE = "TYPE";

		private static String END = "END";

		private ElementClassLoader declareLoader = new IgnoreKeywordLoader(DECLARE);

		private TypeLoader typeLoader = new TypeLoader();

		private NameLoader nameLoader = new NameLoader();

		private ElementClassLoader endLoader = new IgnoreKeywordLoader(END);

		private FactPropertyClassLoader propertyLoader = new FactPropertyClassLoader();

		public FactClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			return startWith(offset, DECLARE, characters);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList(declareLoader).iterator();

			} else if (before.equals(declareLoader)) {
				return Collections.singletonList((ElementClassLoader) typeLoader).iterator();

			} else if (before.equals(typeLoader)) {
				return Collections.singletonList((ElementClassLoader) nameLoader).iterator();

			} else if (before.equals(nameLoader)) {
				return Collections.singletonList((ElementClassLoader) propertyLoader).iterator();

			} else if (before.equals(propertyLoader)) {
				return Arrays.asList((ElementClassLoader) endLoader, (ElementClassLoader) propertyLoader).iterator();

			} else {
				return Collections.emptyIterator();
			}
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, END, characters, true);
		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			String factCode = null;
			String factName = null;

			factCode = typeLoader.pop().getValue();
			factName = nameLoader.pop().getValue();

			List<FactPropertyClass> properties = new ArrayList<>();
			boolean loop = true;
			do {
				FactPropertyClass propertyClass = propertyLoader.pop();
				if (null == propertyClass) {
					loop = false;
				} else {
					properties.add(propertyClass);
				}

			} while (loop);

			if (null == factCode || null == factName || properties.isEmpty()) {
				throw RuleClassException.syntaxException("");
			}
			FactClass factClass = new FactClassImpl(factCode, factName, properties);
			ruleClass.addFactClass(factClass);
		}

	}

	/**
	 * 事实属性定义加载器
	 */
	public static class FactPropertyClassLoader extends AbstractElementClassLoader
					implements ChildElementClassLoader<FactPropertyClass> {

		private TypeLoader typeLoader = new TypeLoader();

		private CodeLoader codeLoader = new CodeLoader();

		private NameLoader nameLoader = new NameLoader();

		private LinkedList<FactPropertyClass> stack = new LinkedList<>();

		public FactPropertyClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			return typeLoader.support(offset, characters);
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, LF.toString(), characters);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList((ElementClassLoader) typeLoader).iterator();
			} else if (before instanceof TypeLoader) {
				return Collections.singletonList((ElementClassLoader) codeLoader).iterator();
			} else if (before instanceof CodeLoader) {
				return Collections.singletonList((ElementClassLoader) nameLoader).iterator();
			} else {
				return Collections.emptyIterator();
			}
		}

		@Override
		public FactPropertyClass pop() {
			if (stack.isEmpty()) {
				return null;
			}
			return stack.pop();
		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			TypeToken typeToken = typeLoader.pop();
			Token codeToken = codeLoader.pop();
			Token nameToken = nameLoader.pop();

			FactPropertyClassImpl property = new FactPropertyClassImpl(typeToken.getType(),
							typeToken.getKeyType(),
							typeToken.getValueType(),
							codeToken.getValue(),
							nameToken.getValue());
			stack.push(property);
		}

	}

	/**
	 * 输入参数定义加载器
	 */
	public static class ArgumentClassLoader extends AbstractElementClassLoader {

		private static String DECLARE = "ARG";

		private ElementClassLoader declareLoader = new IgnoreKeywordLoader(DECLARE);

		private TypeLoader argumentTypeLoader = new TypeLoader();

		private CodeLoader argumentCodeLoader = new CodeLoader();

		private ElementClassLoader commaLoader = new IgnoreKeywordLoader(COMMA.toString());

		public ArgumentClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			return startWith(offset, DECLARE, characters);
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, LF.toString(), characters);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList(declareLoader).iterator();

			} else if (before.equals(declareLoader)) {
				return Collections.singletonList((ElementClassLoader) argumentTypeLoader).iterator();

			} else if (before.equals(argumentTypeLoader)) {
				return Collections.singletonList((ElementClassLoader) argumentCodeLoader).iterator();

			} else if (before.equals(argumentCodeLoader)) {
				return Collections.singletonList(commaLoader).iterator();

			} else if (before.equals(commaLoader)) {
				return Collections.singletonList((ElementClassLoader) argumentTypeLoader).iterator();

			} else {
				return Collections.emptyIterator();
			}

		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			List<ArgumentClass> arguments = new ArrayList<>();

			boolean loop = true;
			do {
				TypeToken argumentType = argumentTypeLoader.pop();
				Token argumentCode = argumentCodeLoader.pop();
				if (null == argumentType && null == argumentCode) {
					loop = false;
				} else if (null == argumentType || null == argumentCode) {
					throw RuleClassException.syntaxException("输入参数定义语法错误");
				} else {
					ArgumentClassImpl argument = new ArgumentClassImpl(argumentType.getValue(), argumentCode.getValue());
					arguments.add(argument);
				}

			} while (loop);

			ruleClass.addArgumentClasses(arguments);
		}
	}

	/**
	 * 返回结果定义加载器
	 */
	public static class ReturnClassLoader extends AbstractElementClassLoader {

		public static String DECLARE = "RETURN";

		private ElementClassLoader ignoreKeywordLoader = new IgnoreKeywordLoader(DECLARE);

		private TypeLoader typeLoader = new TypeLoader();

		private CodeLoader codeLoader = new CodeLoader();

		public ReturnClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			return startWith(offset, DECLARE, characters);
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, LF.toString(), characters);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList(ignoreKeywordLoader).iterator();

			} else if (before.equals(ignoreKeywordLoader)) {
				return Collections.singletonList((ElementClassLoader) typeLoader).iterator();

			} else if (before.equals(typeLoader)) {
				return Collections.singletonList((ElementClassLoader) codeLoader).iterator();

			} else {
				return Collections.emptyIterator();
			}

		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			ReturnClass returnClass = new ReturnClassImpl(typeLoader.pop().getValue(), codeLoader.pop().getValue());
			ruleClass.setReturn(returnClass);
		}

	}

	/**
	 * 规则变量加载器
	 */
	public static class VariableClassLoader extends AbstractElementClassLoader {

		private static String DECLARE = "CONST";

		private ElementClassLoader declareLoader = new IgnoreKeywordLoader(DECLARE);

		private TypeLoader typeLoader = new TypeLoader();

		private CodeLoader codeLoader = new CodeLoader();

		private NameLoader nameLoader = new NameLoader();

		private ElementClassLoader assignLoader = new IgnoreKeywordLoader(ASSIGN.toString());

		private ExpressionLoader expressionLoader = new ExpressionLoader();

		public VariableClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			return startWith(offset, DECLARE, characters);
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, LF.toString(), characters, true);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList(declareLoader).iterator();

			} else if (before.equals(declareLoader)) {
				return Collections.singletonList((ElementClassLoader) typeLoader).iterator();

			} else if (before.equals(typeLoader)) {
				return Collections.singletonList((ElementClassLoader) codeLoader).iterator();

			} else if (before.equals(codeLoader)) {
				return Collections.singletonList((ElementClassLoader) nameLoader).iterator();

			} else if (before.equals(nameLoader)) {
				return Collections.singletonList((ElementClassLoader) assignLoader).iterator();

			} else if (before.equals(assignLoader)) {
				return Collections.singletonList((ElementClassLoader) expressionLoader).iterator();

			} else {
				return Collections.emptyIterator();
			}
		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			boolean hasNext = true;
			do {
				TypeToken type = typeLoader.pop();
				Token code = codeLoader.pop();
				Token name = nameLoader.pop();
				ExpressionToken expressionToken = expressionLoader.pop();

				if (null == type && null == code && null == name && null == expressionToken) {
					hasNext = false;
				} else if (null != type && null != code && null != name && null != expressionToken) {
					ExpressionClass expressionClass = new VariableExpressionClass(expressionToken.getTokens());
					VariableClassImpl variable = new VariableClassImpl(type.getType(),
									type.getKeyType(),
									type.getValueType(),
									code.getValue(),
									name.getValue(),
									expressionClass);
					ruleClass.addVariableClass(variable);
				} else {
					throw RuleClassException.syntaxException("变量解析异常");
				}

			} while (hasNext);
		}

	}

	/**
	 * 规则条件加载器
	 */
	public static class ConditionClassLoader extends AbstractElementClassLoader {

		private static final String DECLARE = "IF";

		private static final String END = "END";

		private static final String THEN = "THEN";

		private final ElementClassLoader declareLoader = new IgnoreKeywordLoader(DECLARE);

		private final ExpressionLoader expressionLoader = new ExpressionLoader();

		private final ElementClassLoader thenLoader = new IgnoreKeywordLoader(THEN);

		private final ExpressionLoader actionLoader = new ExpressionLoader();

		private final ElementClassLoader endLoader = new IgnoreKeywordLoader(END);

		public ConditionClassLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			return startWith(offset, DECLARE, characters);
		}

		@Override
		protected int read(int offset, List<Character> characters) throws RuleClassException {
			return readUntil(offset, END, characters, true);
		}

		@Override
		protected Iterator<ElementClassLoader> iterator(ElementClassLoader before) {
			if (null == before) {
				return Collections.singletonList(declareLoader).iterator();

			} else if (before.equals(declareLoader)) {
				return Collections.singletonList((ElementClassLoader) expressionLoader).iterator();

			} else if (before.equals(expressionLoader)) {
				return Collections.singletonList(thenLoader).iterator();

			} else if (before.equals(thenLoader)) {
				return Collections.singletonList((ElementClassLoader) actionLoader).iterator();

			} else if (before.equals(actionLoader)) {
				return Arrays.asList(endLoader, (ElementClassLoader) actionLoader).iterator();

			} else {
				return Collections.emptyIterator();
			}
		}

		@Override
		protected void loaded(AbstractRuleClass ruleClass) throws RuleClassException {
			ExpressionToken expressionToken = expressionLoader.pop();
			ExpressionClass conditionalExpressionClass = new CacheConditionExpressionClass(expressionToken.getTokens());

			List<ExpressionClass> actions = new ArrayList<>();
			boolean loop = true;
			do {
				ExpressionToken token = actionLoader.pop();
				if (null == token) {
					loop = false;
				} else {
					ExpressionClass action = new ConditionalActionExpressionClass(token.getTokens());
					actions.add(action);
				}
			} while (loop);

			ConditionClass conditionClass = new ConditionClassImpl(conditionalExpressionClass, actions);
			ruleClass.addConditionClass(conditionClass);
		}

	}

	/**
	 * 抽象元素加载器
	 */
	public static abstract class AbstractElementClassLoader implements ElementClassLoader {

		public AbstractElementClassLoader() {

		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			int end = read(offset, characters);

			ElementClassLoader childLoader = null;
			List<Character> subCharacters = characters.subList(offset, end);
			Iterator<ElementClassLoader> iterator = iterator(null);

			int index = 0;
			while (index < subCharacters.size() && iterator.hasNext()) {
				Character character = subCharacters.get(index);
				if (Character.isWhitespace(character) || character == LF) {
					// 过滤 空格/换行
					index += 1;

				} else if (character == COMMENT) {
					// 过滤 注释
					index = readUntil(index, LF.toString(), characters, true);

				} else if ((childLoader = iterator.next()).support(index, subCharacters)) {
					index = childLoader.load(index, subCharacters, ruleClass);
					iterator = iterator(childLoader);

				} else {
					// do nothing
				}
			}
			while (index < subCharacters.size()) {
				Character character = subCharacters.get(index);
				if (Character.isWhitespace(character) || character == LF) {
					// 过滤 空格/换行
					index += 1;
				}
			}
			if (index < subCharacters.size()) {
				throw RuleClassException.syntaxException("语法错误。位置：" + index);
			}
			loaded(ruleClass);
			return end;
		}

		/**
		 * 词法分析后执行动作
		 *
		 * @param ruleClass 规则定义
		 * @throws RuleClassException 规则定义相关异常
		 */
		protected abstract void loaded(AbstractRuleClass ruleClass) throws RuleClassException;

		/**
		 * 预读取特定区域的字符集
		 *
		 * @param offset     字符偏移量
		 * @param characters 字符集
		 * @return 特定区域字符结束位置
		 * @throws RuleClassException 规则定义相关异常
		 */
		protected abstract int read(int offset, List<Character> characters) throws RuleClassException;

		/**
		 * 返回区域加载器迭代器
		 *
		 * @param before
		 * @return 区域加载器
		 * @throws RuleClassException 规则定义相关异常
		 */
		protected abstract Iterator<ElementClassLoader> iterator(ElementClassLoader before) throws RuleClassException;
	}

	/**
	 * 表达式加载类
	 */
	public static class ExpressionLoader implements ChildElementClassLoader<ExpressionToken> {

		private LinkedList<ExpressionToken> stack = new LinkedList<>();

		public ExpressionLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			return true;
		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			int end = offset;
			List<Token> tokens = new ArrayList<>();
			int max = characters.size();
			while (end < max) {
				Character character = characters.get(end);
				if (character == LF) {
					// 换行退出
					break;
				}
				if (Character.isWhitespace(character)) {
					// 过滤空白
					end += 1;

				} else if (character == COMMA) {
					// 读取逗号
					tokens.add(new Token(characters.subList(end, end + 1), TokenKind.COMMA));
					end += 1;

				} else if (Character.isLetter(character)) {
					// 读取变量
					int index = readDynamicEOF(end, (i, c) -> {
						return !Character.isLetter(c) && !Character.isDigit(c);
					}, characters);
					tokens.add(new Token(characters.subList(end, index), TokenKind.VAR));
					end = index;

				} else if (character == '"') {
					// 读取字符串
					int index = readDynamicEOF(end + 1, (i, c) -> {
						return c == '"';
					}, characters);
					index += 1;
					tokens.add(new Token(characters.subList(end, index), TokenKind.STRING));
					end = index;

				} else if (Character.isDigit(character)) {
					// 读取数字
					int index = readDigit(characters, end, max);
					tokens.add(new Token(characters.subList(end, index), TokenKind.NUMBER));
					end = index;

				} else if (character == '(') {
					tokens.add(new Token(character.toString(), TokenKind.LPAREN));
					end += 1;

				} else if (character == ')') {
					tokens.add(new Token(character.toString(), TokenKind.RPAREN));
					end += 1;

				} else if (character == '.') {
					tokens.add(new Token(character.toString(), TokenKind.DOT));
					end += 1;

				} else if (character == '[') {
					tokens.add(new Token(character.toString(), TokenKind.LBRACKET));
					end += 1;

				} else if (character == ']') {
					tokens.add(new Token(character.toString(), TokenKind.RBRACKET));
					end += 1;

				} else if (character == '&') {
					// 读取逻辑 and
					Character next = characters.get(end + 1);
					if (next == '&') {
						tokens.add(new Token("&&", TokenKind.AND));
					} else {
						throw RuleClassException.syntaxException(end, "&");
					}
					end += 2;

				} else if (character == '|') {
					// 读取逻辑 or
					Character next = characters.get(end + 1);
					if (next == '|') {
						tokens.add(new Token("||", TokenKind.OR));
					} else {
						throw RuleClassException.syntaxException(end, "|");
					}
					end += 2;

				} else if (character == '!') {
					// 读取逻辑 非 / 不等于
					Character next = characters.get(end + 1);
					if (next == '=') {
						tokens.add(new Token("!=", TokenKind.NE));
						end += 2;
					} else {
						tokens.add(new Token("!", TokenKind.NOT));
						end += 1;
					}

				} else if (character == '>') {
					// 读取大于 / 大于等于
					Character next = characters.get(end + 1);
					if (next == '=') {
						tokens.add(new Token(">=", TokenKind.GE));
						end += 2;
					} else {
						tokens.add(new Token(">", TokenKind.GT));
						end += 1;
					}

				} else if (character == '<') {
					// 读取小于 / 小于等于
					Character next = characters.get(end + 1);
					if (next == '=') {
						tokens.add(new Token("<=", TokenKind.LE));
						end += 2;
					} else {
						tokens.add(new Token("<", TokenKind.LT));
						end += 1;
					}

				} else if (character == '=') {
					// 读取等于
					Character next = characters.get(end + 1);
					if (next == '=') {
						tokens.add(new Token("==", TokenKind.EQ));
						end += 2;
					} else {
						throw RuleClassException.syntaxException(end, "==");
					}

				} else if (character == '+') {
					// 读取 加法 运算
					tokens.add(new Token(character.toString(), TokenKind.PLUS));
					end += 1;

				} else if (character == '-') {
					// 读取 减法 运算
					tokens.add(new Token(character.toString(), TokenKind.MINUS));
					end += 1;
				} else if (character == '*') {
					// 读取 乘法 运算
					tokens.add(new Token(character.toString(), TokenKind.MUL));
					end += 1;

				} else if (character == '/') {
					// 读取 除法 运算
					tokens.add(new Token(character.toString(), TokenKind.DIV));
					end += 1;

				} else if (character == '^') {
					// 读取 平方 运算
					tokens.add(new Token(character.toString(), TokenKind.POWER));
					end += 1;
				} else if (character == '%') {
					// 读取 取余 运算
					tokens.add(new Token(character.toString(), TokenKind.MOD));
					end += 1;
				} else {
					throw RuleClassException.syntaxException(end, "非法字符");
				}
			}
			stack.push(new ExpressionToken(characters.subList(offset, end), tokens, TokenKind.EXPR));
			return end;
		}

		private int readDigit(List<Character> characters, int end, int max) throws RuleClassException {
			int index = readDynamicEOF(end, (i, c) -> {
				return !Character.isDigit(c);
			}, characters);
			if (index < max && characters.get(index) == '.') {
				index = readDynamicEOF(index + 1, (i, c) -> {
					return !Character.isDigit(c);
				}, characters);
				Character endChar = characters.get(index);
				if (endChar != 'd' && endChar != 'f' && endChar != 'b') {
					throw RuleClassException.syntaxException(index, endChar.toString());
				}
				index += 1;
			}
			return index;
		}

		/**
		 * 读取动态结束标志
		 *
		 * @param offset     起始字符偏移位置
		 * @param eof        动态结束符
		 * @param characters 字符集
		 * @return 结束符位置
		 * @throws RuleClassException 规则定义异常
		 */
		private int readDynamicEOF(int offset, EOF eof, List<Character> characters)
						throws RuleClassException {
			int index = offset;
			while (index < characters.size()) {
				Character character = characters.get(index);
				if (eof.apply(index, character)) {
					return index;
				}
				index += 1;
			}
			if (index == characters.size()) {
				return index;
			}
			throw RuleClassException.syntaxException(offset, "未找到匹配的闭合字符");
		}

		@Override
		public ExpressionToken pop() {
			if (stack.isEmpty()) {
				return null;
			}
			return stack.pop();
		}

	}

	/**
	 * 表达式Token
	 */
	public static class ExpressionToken extends Token {

		private List<Token> tokens;

		public ExpressionToken(List<Character> value, List<Token> tokens, TokenKind kind) {
			super(value, kind);
			this.tokens = tokens;
		}

		public List<Token> getTokens() {
			return tokens;
		}

	}

	/**
	 * 名称加载器
	 */
	public static class NameLoader implements ChildElementClassLoader<Token> {

		private LinkedList<Token> stack = new LinkedList<>();

		public NameLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			Character character = characters.get(offset);
			return character == BOUND;
		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			int end = readUntil(offset + 1, BOUND.toString(), characters);
			stack.push(new Token(characters.subList(offset + 1, end), TokenKind.Name));
			end += 1;
			return end;
		}

		@Override
		public Token pop() {
			if (stack.isEmpty()) {
				return null;
			}
			return stack.pop();
		}

	}

	/**
	 * 编码加载器
	 */
	public static class CodeLoader implements ChildElementClassLoader<Token> {

		private LinkedList<Token> stack = new LinkedList<>();

		public CodeLoader() {
		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			Character character = characters.get(offset);
			return Character.isLowerCase(character);
		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			int end = readUntil(offset, (c) -> !Character.isLetter(c), characters);
			List<Character> subCharacters = characters.subList(offset, end);
			for (Character character : subCharacters) {
				if (!Character.isLetter(character)) {
					throw RuleClassException.syntaxException("编码仅支持字母组合，且首字母小写。 当前值：" + character);
				}
			}
			stack.push(new Token(subCharacters, TokenKind.Code));
			return end;
		}

		@Override
		public Token pop() {
			if (stack.isEmpty()) {
				return null;
			}
			return stack.pop();
		}

	}

	/**
	 * 类型加载器
	 */
	public static class TypeLoader implements ChildElementClassLoader<TypeToken> {

		private LinkedList<TypeToken> stack = new LinkedList<>();

		public TypeLoader() {

		}

		@Override
		public boolean support(int offset, List<Character> characters) {
			Character character = characters.get(offset);
			return Character.isUpperCase(character);
		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			int end = offset;
			boolean hasComponentType = false;
			while (end < characters.size()) {
				Character character = characters.get(end);
				if (Character.isLetter(character)) {
					end += 1;
				} else if (Character.isWhitespace(character)) {
					break;
				} else if (character == '<' && !hasComponentType) {
					// FIX ME: 这里的算法暂不支持元素类型为List，Map， 待后续补充
					end = readUntil(end, ">", characters);
					hasComponentType = true;
					end += 1;
				} else {
					throw RuleClassException.syntaxException("类型定义格式异常。位置：" + end + "，字符：" + character);
				}
			}
			if (end == offset) {
				throw RuleClassException.syntaxException("类型定义格式异常。位置：" + end + "，字符：" + characters.get(end));
			}
			stack.push(new TypeToken(characters.subList(offset, end), TokenKind.Type));
			return end;
		}

		@Override
		public TypeToken pop() {
			if (stack.isEmpty()) {
				return null;
			}
			return stack.pop();
		}

	}

	/**
	 * 关键字加载器
	 */
	public static class IgnoreKeywordLoader implements ElementClassLoader {

		private String keyword;

		public IgnoreKeywordLoader(String keyword) {
			this.keyword = keyword;
		}

		@Override
		public boolean support(int offset, List<Character> characters) throws RuleClassException {
			return startWith(offset, keyword, characters);
		}

		@Override
		public int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException {
			return offset + keyword.length();
		}

	}

	/**
	 * 规则定义相关元素加载器
	 */
	public static interface ElementClassLoader {

		/**
		 * 判断指定 {@param offset} 偏移量字符，是否符合规则定义相关字符
		 *
		 * @param offset     偏移量
		 * @param characters 字符集
		 * @return 是否符合规则定义相关字符，是：符合，否：不符合
		 * @throws RuleClassException 规则定义相关异常
		 */
		boolean support(int offset, List<Character> characters) throws RuleClassException;

		/**
		 * 加载规则定义元素
		 *
		 * @param offset     起始偏移量
		 * @param characters 字符集
		 * @param ruleClass  规则定义
		 * @return 规则定义元素的结束偏移量
		 * @throws RuleClassException 规则定义相关异常
		 */
		int load(int offset, List<Character> characters, AbstractRuleClass ruleClass) throws RuleClassException;

	}

	/**
	 * 子元素加载器
	 */
	public static interface ChildElementClassLoader<T> extends ElementClassLoader {

		/**
		 * @return 子元素
		 */
		public T pop();

	}

	public static interface EOF {

		public boolean apply(int offset, Character c) throws RuleClassException;

	}

}
