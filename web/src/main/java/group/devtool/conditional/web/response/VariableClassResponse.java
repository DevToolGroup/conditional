package group.devtool.conditional.web.response;

import group.devtool.conditional.web.request.ExpressionRequest;
import lombok.Data;

@Data
public class VariableClassResponse {

	private Integer id;

	private String code;

	private ExpressionRequest expression;

	private Integer order;

}
