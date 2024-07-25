package group.devtool.conditional.web.response;

import group.devtool.conditional.web.request.ExpressionRequest;
import lombok.Data;

import java.util.List;

@Data
public class ConditionClassResponse {

	private Integer id;

	private ExpressionRequest condition;

	private List<ExpressionRequest> functions;

	private Integer order;

}
