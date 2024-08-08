package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "version_id", nullable = false)
	private Integer versionId;

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;

}
