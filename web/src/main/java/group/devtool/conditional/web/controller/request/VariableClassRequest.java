package group.devtool.conditional.web.request;

import lombok.Data;

@Data
public class VariableClassRequest {

	private Integer id;

	private String code;

	private ExpressionRequest expression;

	private Integer order;
}
