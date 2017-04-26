package run.order66.application.repository;

import run.order66.application.domain.RuleReport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleReport entity.
 */
@SuppressWarnings("unused")
public interface RuleReportRepository extends JpaRepository<RuleReport,Long> {

    @Query("select ruleReport from RuleReport ruleReport where ruleReport.user.login = ?#{principal.username}")
    List<RuleReport> findByUserIsCurrentUser();

    @Query("select r from RuleReport r where r.KEY = ?1")
    List<RuleReport> findOneByKeyindex(String key);
}
