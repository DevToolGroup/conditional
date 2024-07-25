package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.RuleClassVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface RuleClassVersionRepository extends JpaRepository<RuleClassVersionEntity, Integer>, JpaSpecificationExecutor<RuleClassVersionEntity> {

	RuleClassVersionEntity findByIdAndRuleIdAndDeleted(Integer versionId, Integer ruleId, String deleted);

	RuleClassVersionEntity findByRuleIdAndStatus(Integer ruleId, String status);

	@Modifying
	@Query("update conditional_rule_class_version " +
					"set " +
					"	deleted = 'N', " +
					"	updateTime = :updateTime," +
					"	lock = :lock + 1 " +
					"where " +
					"	id = :versionId " +
					"	and rule_id = :ruleId " +
					"	and lock = :lock " +
					"	and status in ('OFFLINE', 'DESIGN')" +
					"	and deleted = 'N'")
	int deleteVersion(Long lock, Long updateTime, Integer versionId, Integer ruleId);

	@Modifying
	@Query("update conditional_rule_class_version " +
					"set " +
					"	status = OFFLINE, " +
					"	updateTime = :updateTime," +
					"	lock = :lock + 1 " +
					"where " +
					"	id = :versionId " +
					"	and rule_id = :ruleId " +
					"	and lock = :lock " +
					"	and deleted = 'N' " +
					"	and status = 'ONLINE'")
	int changeDeployedVersion(Long updatedTime, Long lock, Integer versionId, Integer ruleId);

	@Modifying
	@Query("update conditional_rule_class_version " +
					"set " +
					"	lock = :lock + 1, " +
					"where " +
					"		id = :id " +
					"		and deleted = 'N'" +
					"		and lock = :lock ")
	int lock(Integer versionId, Integer ruleId, Long lock);

	List<RuleClassVersionEntity> findByRuleIdAndDeleted(Integer ruleId, String deleted);

	default void doLockVersion(Integer versionId, Integer ruleId, ConditionalStatus... statuses) {
		RuleClassVersionEntity version = findByIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		if (version == null) {
			throw new ConditionalException("规则版本不存在");
		}
		// 检查规则定义是否已发布，已发布提示错误：规则已发布，不允许修改
		if (Arrays.stream(statuses).noneMatch(i -> version.getStatus().equals(i.name()))) {
			throw new ConditionalException("规则版本状态不支持当前操作");
		}
		int rows = lock(versionId, ruleId, version.getLock());
		if (rows == 0) {
			throw new ConditionalException("规则版本已被修改，请刷新后重试");
		}
	}
}
