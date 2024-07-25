package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.ReturnClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnClassRepository extends JpaRepository<ReturnClassEntity, Integer>, JpaSpecificationExecutor<ReturnClassEntity> {

	List<ReturnClassEntity> findByRuleIdAndVersionIdAndTypeAndDeleted(Integer ruleId, Integer versionId, String code, String deleted);

	ReturnClassEntity findByVersionIdAndRuleIdAndDeleted(Integer versionId, Integer ruleId, String deleted);

	ReturnClassEntity findByIdAndVersionIdAndRuleIdAndDeleted(Integer returnId, Integer versionId, Integer ruleId, String deleted);
}
