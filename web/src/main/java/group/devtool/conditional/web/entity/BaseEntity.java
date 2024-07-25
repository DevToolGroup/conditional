package group.devtool.conditional.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "deleted", nullable = false, length = 1)
	private String deleted;

	@Column(name = "createTime", nullable = false)
	private Long createTime;

	@Column(name = "updateTime", nullable = false)
	private Long updateTime;

}
