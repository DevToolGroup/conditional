package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.ArgumentClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArgumentClassRepository extends JpaRepository<ArgumentClassEntity, Integer>, JpaSpecificationExecutor<ArgumentClassEntity> {

	ArgumentClassEntity findByRuleIdAndVersionIdAndCode(Integer ruleId, Integer versionId, String code);

	ArgumentClassEntity findByIdAndRuleIdAndVersionId(Integer argumentId, Integer ruleId, Integer versionId);

	List<ArgumentClassEntity> findByRuleIdAndVersionIdAndTypeAndDeleted(Integer ruleId, Integer versionId, String code, String deleted);
}
