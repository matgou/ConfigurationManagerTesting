package run.order66.application.repository;

import run.order66.application.domain.Scheduling;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Scheduling entity.
 */
@SuppressWarnings("unused")
public interface SchedulingRepository extends JpaRepository<Scheduling,Long> {

    @Query("select distinct scheduling from Scheduling scheduling left join fetch scheduling.rules")
    List<Scheduling> findAllWithEagerRelationships();

    @Query("select scheduling from Scheduling scheduling left join fetch scheduling.rules where scheduling.id =:id")
    Scheduling findOneWithEagerRelationships(@Param("id") Long id);
}
