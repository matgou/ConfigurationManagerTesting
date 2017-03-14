package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleReport entity.
 */
@SuppressWarnings("unused")
public interface RuleReportRepository extends JpaRepository<RuleReport,Long> {

}
