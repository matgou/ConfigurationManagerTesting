package run.order66.application.service;

import run.order66.application.domain.RuleReport;
import run.order66.application.repository.RuleReportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing RuleReport.
 */
@Service
@Transactional
public class RuleReportService {

    private final Logger log = LoggerFactory.getLogger(RuleReportService.class);
    
    private final RuleReportRepository ruleReportRepository;

    public RuleReportService(RuleReportRepository ruleReportRepository) {
        this.ruleReportRepository = ruleReportRepository;
    }

    /**
     * Save a ruleReport.
     *
     * @param ruleReport the entity to save
     * @return the persisted entity
     */
    public RuleReport save(RuleReport ruleReport) {
        log.debug("Request to save RuleReport : {}", ruleReport);
        RuleReport result = ruleReportRepository.save(ruleReport);
        return result;
    }

    /**
     *  Get all the ruleReports.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RuleReport> findAll(Pageable pageable) {
        log.debug("Request to get all RuleReports");
        Page<RuleReport> result = ruleReportRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one ruleReport by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RuleReport findOne(Long id) {
        log.debug("Request to get RuleReport : {}", id);
        RuleReport ruleReport = ruleReportRepository.findOne(id);
        return ruleReport;
    }

    /**
     *  Delete the  ruleReport by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RuleReport : {}", id);
        ruleReportRepository.delete(id);
    }

	public RuleReport findOneFromIndexkey(String key) {
		// TODO Auto-generated method stub
		List<RuleReport> ruleReports = ruleReportRepository.findOneByKeyindex(key);
		if(ruleReports.size() > 0) {
			return ruleReports.get(0);
		}
		return null;
	}
}
