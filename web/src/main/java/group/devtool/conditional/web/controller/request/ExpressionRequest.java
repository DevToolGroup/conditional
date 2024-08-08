package group.devtool.conditional.web.request;


import lombok.Data;


@Data
public class ExpressionRequest {

	private String expression;

	private ExpressionRequest left;

	private ExpressionRequest right;

	private String operator;

}
