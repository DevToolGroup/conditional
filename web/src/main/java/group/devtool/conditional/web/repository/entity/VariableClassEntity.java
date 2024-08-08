package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "conditional_variable_class")
public class VariableClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false, length = 100)
	private String code;

	@Column(name = "order", nullable = false)
	private Integer order;

	@Column(name = "expression", nullable = false, length = 50000)
	private String expression;

	@Column(name = "version_id", nullable = false)
	private Integer versionId;

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;

}
