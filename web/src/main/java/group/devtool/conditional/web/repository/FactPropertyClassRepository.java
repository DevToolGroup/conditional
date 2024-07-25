package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.FactPropertyClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FactPropertyClassRepository extends JpaRepository<FactPropertyClassEntity, Integer>,
				JpaSpecificationExecutor<FactPropertyClassEntity> {

	FactPropertyClassEntity findByIdAndFactIdAndVersionIdAndRuleIdAndDeleted(Integer factPropertyId,
																																					 Integer factId, Integer versionId,
																																					 Integer ruleId, String deleted);

	FactPropertyClassEntity findByCodeAndFactIdAndVersionIdAndRuleIdAndDeleted(String code, Integer factId,
																																						 Integer versionId, Integer ruleId,
																																						 String deleted);
}
