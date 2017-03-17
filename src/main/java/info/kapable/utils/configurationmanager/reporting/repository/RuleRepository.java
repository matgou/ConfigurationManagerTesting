package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * Spring Data JPA repository for the Rule entity.
 */
@SuppressWarnings("unused")
public interface RuleRepository extends JpaRepository<Rule,Long> {


 	@Query(value = "SELECT r.*, (SELECT rr FROM CCOL_CM_RULEREPORT rr WHERE rr.rule_id = r.id ORDER BY REPORT_DATE DESC LIMIT 1) FROM CCOL_CM_RULE r", nativeQuery = true)
	Object[] findAllWithLastResult();
	
}
