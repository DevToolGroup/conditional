package group.devtool.conditional.web.request;

import lombok.Data;

import java.util.List;

@Data
public class ConditionClassRequest {

	private Integer id;

	private ExpressionRequest condition;

	private List<ExpressionRequest> functions;

	private Integer order;

}
