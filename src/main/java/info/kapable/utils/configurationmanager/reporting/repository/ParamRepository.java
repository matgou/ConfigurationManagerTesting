package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.Param;

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
