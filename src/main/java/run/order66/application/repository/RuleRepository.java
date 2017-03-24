package run.order66.application.repository;

import run.order66.application.domain.Rule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rule entity.
 */
@SuppressWarnings("unused")
public interface RuleRepository extends JpaRepository<Rule,Long> {

	@Query("SELECT r FROM Rule r WHERE r.process.id=?1")
	List<Rule> findByProcess(Long processId);

}
