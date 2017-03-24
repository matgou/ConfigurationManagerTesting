package run.order66.application.repository;

import run.order66.application.domain.Process;
import run.order66.application.domain.Scheduling;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Process entity.
 */
@SuppressWarnings("unused")
public interface ProcessRepository extends JpaRepository<Process,Long> {
	
    @Query("select p from Process p where p.parent is null")
    Page<Process> findAllRoot(Pageable pageable);

    @Query("select p from Process p where p.parent.id = :processId")
    List<Process> findAllRoot(@Param("processId") Long processId);
}
