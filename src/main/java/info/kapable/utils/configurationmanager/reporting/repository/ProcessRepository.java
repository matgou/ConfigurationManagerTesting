package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.Process;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Process entity.
 */
@SuppressWarnings("unused")
public interface ProcessRepository extends JpaRepository<Process,Long> {

}
