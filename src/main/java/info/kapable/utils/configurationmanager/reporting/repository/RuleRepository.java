package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rule entity.
 */
@SuppressWarnings("unused")
public interface RuleRepository extends JpaRepository<Rule,Long> {

}
