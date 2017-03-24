package run.order66.application.repository;

import run.order66.application.domain.RuleType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleType entity.
 */
@SuppressWarnings("unused")
public interface RuleTypeRepository extends JpaRepository<RuleType,Long> {

}
