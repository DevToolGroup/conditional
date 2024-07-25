package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.ConditionClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConditionClassRepository extends JpaRepository<ConditionClassEntity, Integer>,
				JpaSpecificationExecutor<ConditionClassEntity> {

	ConditionClassEntity findByIdAndVersionIdAndRuleIdAndDeleted(Integer conditionId, Integer versionId, Integer ruleId, String deleted);

	ConditionClassEntity findByVersionIdAndRuleIdAndOrderAndDeleted(Integer versionId, Integer ruleId, Integer order, String deleted);
}
