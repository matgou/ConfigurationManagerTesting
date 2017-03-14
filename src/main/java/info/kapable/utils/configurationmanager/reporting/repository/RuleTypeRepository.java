package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.RuleType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleType entity.
 */
@SuppressWarnings("unused")
public interface RuleTypeRepository extends JpaRepository<RuleType,Long> {

}
