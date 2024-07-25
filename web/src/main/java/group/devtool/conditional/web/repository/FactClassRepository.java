package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.FactClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FactClassRepository extends JpaRepository<FactClassEntity, Integer>, JpaSpecificationExecutor<FactClassEntity> {

	FactClassEntity findByCodeAndVersionIdAndRuleId(String code, Integer versionId, Integer ruleId);

	FactClassEntity findByIdAndVersionIdAndRuleId(Integer id, Integer versionId, Integer ruleId);
}
