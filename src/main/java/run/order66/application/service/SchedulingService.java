package run.order66.application.service;

import run.order66.application.domain.Rule;
import run.order66.application.domain.Scheduling;
import run.order66.application.exception.UnsuportedTriggerException;
import run.order66.application.repository.SchedulingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Scheduling.
 */
@Service
@Transactional
public class SchedulingService {

	private final Logger log = LoggerFactory.getLogger(SchedulingService.class);

	private final SchedulingRepository schedulingRepository;

	@Autowired
	private SchedulingTaskService schedulingTask;

	public SchedulingService(SchedulingRepository schedulingRepository) {
		this.schedulingRepository = schedulingRepository;
	}

	/**
	 * Save a scheduling.
	 *
	 * @param scheduling
	 *            the entity to save
	 * @return the persisted entity
	 */
	public Scheduling save(Scheduling scheduling) {
		log.debug("Request to save Scheduling : {}", scheduling);
		Scheduling result = null;
		try {
			schedulingTask.unregisterJobFromScheduling(scheduling);
			for (Rule rule : scheduling.getRules()) {
				schedulingTask.createTrigger(rule, scheduling);
			}
			result = schedulingRepository.save(scheduling);
		} catch (UnsuportedTriggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get all the schedulings.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Scheduling> findAll(Pageable pageable) {
		log.debug("Request to get all Schedulings");
		Page<Scheduling> result = schedulingRepository.findAll(pageable);
		return result;
	}

	/**
	 * Get one scheduling by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Scheduling findOne(Long id) {
		log.debug("Request to get Scheduling : {}", id);
		Scheduling scheduling = schedulingRepository.findOneWithEagerRelationships(id);
		return scheduling;
	}

	/**
	 * Delete the scheduling by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Scheduling : {}", id);
		Scheduling scheduling = this.findOne(id);
		this.schedulingTask.unregisterJobFromScheduling(scheduling);
		schedulingRepository.delete(id);
	}

	public List<Scheduling> findAll() {
		return schedulingRepository.findAll();
	}

	public List<Scheduling> findAllFetchRules() {
		log.debug("Request to find all scheduling join rules");
		return this.schedulingRepository.findAllWithEagerRelationships();
	}
}
