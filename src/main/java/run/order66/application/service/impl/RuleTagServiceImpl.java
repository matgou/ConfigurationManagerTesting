package run.order66.application.service.impl;

import run.order66.application.service.RuleTagService;
import run.order66.application.domain.RuleTag;
import run.order66.application.repository.RuleTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing RuleTag.
 */
@Service
@Transactional
public class RuleTagServiceImpl implements RuleTagService{

    private final Logger log = LoggerFactory.getLogger(RuleTagServiceImpl.class);
    
    private final RuleTagRepository ruleTagRepository;

    public RuleTagServiceImpl(RuleTagRepository ruleTagRepository) {
        this.ruleTagRepository = ruleTagRepository;
    }

    /**
     * Save a ruleTag.
     *
     * @param ruleTag the entity to save
     * @return the persisted entity
     */
    @Override
    public RuleTag save(RuleTag ruleTag) {
        log.debug("Request to save RuleTag : {}", ruleTag);
        RuleTag result = ruleTagRepository.save(ruleTag);
        return result;
    }

    /**
     *  Get all the ruleTags.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RuleTag> findAll() {
        log.debug("Request to get all RuleTags");
        List<RuleTag> result = ruleTagRepository.findAll();

        return result;
    }

    /**
     *  Get one ruleTag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RuleTag findOne(Long id) {
        log.debug("Request to get RuleTag : {}", id);
        RuleTag ruleTag = ruleTagRepository.findOne(id);
        return ruleTag;
    }

    /**
     *  Delete the  ruleTag by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RuleTag : {}", id);
        ruleTagRepository.delete(id);
    }

	@Override
	public List<RuleTag> findDistinct() {
		log.debug("Request to distinct RuleTag");
		List<RuleTag> tags = new ArrayList<RuleTag>();
        
		List<String> distinctName = ruleTagRepository.findDistinct();
        
        for(String name: distinctName) {
        	tags.add(new RuleTag(name));
        }
		return tags;
	}
}
