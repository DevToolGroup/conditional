package group.devtool.conditional.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "conditional_rule_class_version")
public class RuleClassVersionEntity extends BaseEntity {

	@Column(name = "rule_id", nullable = false)
	private Integer ruleId;

	// 0 - 设计中
	// 1 - 已发布
	// -1 - 已下线
	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "lock")
	private Long lock;

}

