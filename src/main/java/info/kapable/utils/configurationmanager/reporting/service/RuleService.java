package info.kapable.utils.configurationmanager.reporting.service;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.exception.UnsuportedTriggerException;
import info.kapable.utils.configurationmanager.reporting.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Rule.
 */
@Service
@Transactional
public class RuleService {

	private final Logger log = LoggerFactory.getLogger(RuleService.class);

	private final RuleRepository ruleRepository;

	@Autowired
	private SchedulingTaskService schedulingTask;

	public RuleService(RuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

	/**
	 * Save a rule.
	 *
	 * @param rule
	 *            the entity to save
	 * @return the persisted entity
	 */
	public Rule save(Rule rule) {
		log.debug("Request to save Rule : {}", rule);
		Rule result = null;

		result = ruleRepository.save(rule);
		return result;
	}

	/**
	 * Get all the rules.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Rule> findAll(Pageable pageable) {
		log.debug("Request to get all Rules");
		Page<Rule> result = ruleRepository.findAll(pageable);
		return result;
	}

	/**
	 * Get one rule by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Rule findOne(Long id) {
		log.debug("Request to get Rule : {}", id);
		Rule rule = ruleRepository.findOne(id);
		return rule;
	}

	/**
	 * Delete the rule by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Rule : {}", id);
		Rule rule = this.findOne(id);
		this.schedulingTask.unregisterJobFromRule(rule);
		ruleRepository.delete(id);
	}
}
