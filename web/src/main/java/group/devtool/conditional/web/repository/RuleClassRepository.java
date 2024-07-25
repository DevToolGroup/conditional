package group.devtool.conditional.web.repository;

import group.devtool.conditional.web.entity.RuleClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleClassRepository extends JpaRepository<RuleClassEntity, Integer>, JpaSpecificationExecutor<RuleClassEntity> {

	RuleClassEntity findByCodeAndDeleted(String code, String deleted);
}
