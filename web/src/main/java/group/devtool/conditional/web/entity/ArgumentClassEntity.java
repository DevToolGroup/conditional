package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "conditional_rule_argument_class", uniqueConstraints = @UniqueConstraint(columnNames={"rule_id", "version_id", "code"}))
public class ArgumentClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "version_id", nullable = false)
	private Integer versionId;

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;

}
