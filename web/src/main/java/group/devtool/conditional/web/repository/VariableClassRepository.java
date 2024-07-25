package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.VariableClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableClassRepository extends JpaRepository<VariableClassEntity, Integer>,
				JpaSpecificationExecutor<VariableClassEntity> {

	VariableClassEntity findByCodeAndVersionIdAndRuleIdAndDeleted(String code, Integer versionId, Integer ruleId);

	VariableClassEntity findByIdAndVersionIdAndRuleIdAndDeleted(Integer variableId, Integer versionId, Integer ruleId, String deleted);

	VariableClassEntity findByOrderAndVersionIdAndRuleIdAndDeleted(Integer order, Integer versionId, Integer ruleId);
}
