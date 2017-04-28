package run.order66.application.service;

import run.order66.application.domain.RuleTag;
import java.util.List;

/**
 * Service Interface for managing RuleTag.
 */
public interface RuleTagService {

    /**
     * Save a ruleTag.
     *
     * @param ruleTag the entity to save
     * @return the persisted entity
     */
    RuleTag save(RuleTag ruleTag);

    /**
     *  Get all the ruleTags.
     *  
     *  @return the list of entities
     */
    List<RuleTag> findAll();

    /**
     *  Get the "id" ruleTag.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RuleTag findOne(Long id);

    /**
     *  Delete the "id" ruleTag.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
