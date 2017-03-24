package run.order66.application.repository;

import run.order66.application.domain.Param;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Param entity.
 */
@SuppressWarnings("unused")
public interface ParamRepository extends JpaRepository<Param,Long> {

	@Query("SELECT p FROM Param p WHERE p.key = ?1")
	Param findOneByKey(String key);

}
