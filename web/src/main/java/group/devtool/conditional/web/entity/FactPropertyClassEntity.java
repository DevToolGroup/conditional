package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FactPropertyClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false, length = 100)
	private String code;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "type", nullable = false, length = 100)
	private String type;

	@Column(name = "valueType", nullable = false, length = 100)
	private String valueType;

	@Column(name = "keyType", nullable = false, length = 100)
	private String keyType;

	@Column(name = "fact_id", nullable = false)
	private Integer factId;

	@Column(name = "version_id", nullable = false)
	private Integer versionId;

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;

}
