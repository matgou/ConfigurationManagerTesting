package run.order66.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import run.order66.application.domain.RuleTag;
import run.order66.application.service.RuleTagService;
import run.order66.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RuleTag.
 */
@RestController
@RequestMapping("/api")
public class RuleTagResource {

    private final Logger log = LoggerFactory.getLogger(RuleTagResource.class);

    private static final String ENTITY_NAME = "ruleTag";
        
    private final RuleTagService ruleTagService;

    public RuleTagResource(RuleTagService ruleTagService) {
        this.ruleTagService = ruleTagService;
    }

    /**
     * POST  /rule-tags : Create a new ruleTag.
     *
     * @param ruleTag the ruleTag to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleTag, or with status 400 (Bad Request) if the ruleTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-tags")
    @Timed
    public ResponseEntity<RuleTag> createRuleTag(@Valid @RequestBody RuleTag ruleTag) throws URISyntaxException {
        log.debug("REST request to save RuleTag : {}", ruleTag);
        if (ruleTag.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleTag cannot already have an ID")).body(null);
        }
        RuleTag result = ruleTagService.save(ruleTag);
        return ResponseEntity.created(new URI("/api/rule-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-tags : Updates an existing ruleTag.
     *
     * @param ruleTag the ruleTag to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleTag,
     * or with status 400 (Bad Request) if the ruleTag is not valid,
     * or with status 500 (Internal Server Error) if the ruleTag couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-tags")
    @Timed
    public ResponseEntity<RuleTag> updateRuleTag(@Valid @RequestBody RuleTag ruleTag) throws URISyntaxException {
        log.debug("REST request to update RuleTag : {}", ruleTag);
        if (ruleTag.getId() == null) {
            return createRuleTag(ruleTag);
        }
        RuleTag result = ruleTagService.save(ruleTag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleTag.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-tags : get all the ruleTags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ruleTags in body
     */
    @GetMapping("/rule-tags")
    @Timed
    public List<RuleTag> getAllRuleTags() {
        log.debug("REST request to get all RuleTags");
        return ruleTagService.findDistinct();
    }

    /**
     * GET  /rule-tags/:id : get the "id" ruleTag.
     *
     * @param id the id of the ruleTag to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleTag, or with status 404 (Not Found)
     */
    @GetMapping("/rule-tags/{id}")
    @Timed
    public ResponseEntity<RuleTag> getRuleTag(@PathVariable Long id) {
        log.debug("REST request to get RuleTag : {}", id);
        RuleTag ruleTag = ruleTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleTag));
    }

    /**
     * DELETE  /rule-tags/:id : delete the "id" ruleTag.
     *
     * @param id the id of the ruleTag to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleTag(@PathVariable Long id) {
        log.debug("REST request to delete RuleTag : {}", id);
        ruleTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
