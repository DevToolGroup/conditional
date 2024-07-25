package group.devtool.conditional.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "conditional_rule_class")
public class RuleClassEntity extends BaseEntity {

	@Column(name = "code", nullable = false, length = 300, unique = true)
	private String code;

	@Column(name = "description", nullable = false, length = 1000)
	private String description;

}
