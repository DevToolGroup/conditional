package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionClassEntity extends BaseEntity {

	@Column(name = "condition", nullable = false, length = 50000)
	private String condition;

	@Column(name = "functions", nullable = false, length = 50000)
	private String functions;

	@Column(name = "order", nullable = false)
	private Integer order;

	@Column(name = "versionId", nullable = false)
	private Integer versionId;

	@Column(name = "ruleId", nullable = false)
	private Integer ruleId;

}
