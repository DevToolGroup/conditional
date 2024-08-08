package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FactClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false, length = 100)
	private String code;

	@Column(name = "name", nullable = false, length = 500)
	private String name;

	@Column(name = "version_id", nullable = false)
	private Integer versionId;

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;
}
