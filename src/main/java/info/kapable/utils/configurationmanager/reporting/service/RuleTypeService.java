package info.kapable.utils.configurationmanager.reporting.service;

import info.kapable.utils.configurationmanager.reporting.domain.RuleType;
import info.kapable.utils.configurationmanager.reporting.repository.RuleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing RuleType.
 */
@Service
@Transactional
public class RuleTypeService {

    private final Logger log = LoggerFactory.getLogger(RuleTypeService.class);
    
    private final RuleTypeRepository ruleTypeRepository;

    public RuleTypeService(RuleTypeRepository ruleTypeRepository) {
        this.ruleTypeRepository = ruleTypeRepository;
    }

    /**
     * Save a ruleType.
     *
     * @param ruleType the entity to save
     * @return the persisted entity
     */
    public RuleType save(RuleType ruleType) {
        log.debug("Request to save RuleType : {}", ruleType);
        RuleType result = ruleTypeRepository.save(ruleType);
        return result;
    }

    /**
     *  Get all the ruleTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RuleType> findAll(Pageable pageable) {
        log.debug("Request to get all RuleTypes");
        Page<RuleType> result = ruleTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one ruleType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RuleType findOne(Long id) {
        log.debug("Request to get RuleType : {}", id);
        RuleType ruleType = ruleTypeRepository.findOne(id);
        return ruleType;
    }

    /**
     *  Delete the  ruleType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RuleType : {}", id);
        ruleTypeRepository.delete(id);
    }
}
