package run.order66.application.repository;

import run.order66.application.domain.RuleTag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleTag entity.
 */
@SuppressWarnings("unused")
public interface RuleTagRepository extends JpaRepository<RuleTag,Long> {

}
