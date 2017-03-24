package run.order66.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import run.order66.application.domain.RuleType;
import run.order66.application.service.RuleTypeService;
import run.order66.application.web.rest.util.HeaderUtil;
import run.order66.application.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RuleType.
 */
@RestController
@RequestMapping("/api")
public class RuleTypeResource {

    private final Logger log = LoggerFactory.getLogger(RuleTypeResource.class);

    private static final String ENTITY_NAME = "ruleType";
        
    private final RuleTypeService ruleTypeService;

    public RuleTypeResource(RuleTypeService ruleTypeService) {
        this.ruleTypeService = ruleTypeService;
    }

    /**
     * POST  /rule-types : Create a new ruleType.
     *
     * @param ruleType the ruleType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleType, or with status 400 (Bad Request) if the ruleType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-types")
    @Timed
    public ResponseEntity<RuleType> createRuleType(@Valid @RequestBody RuleType ruleType) throws URISyntaxException {
        log.debug("REST request to save RuleType : {}", ruleType);
        if (ruleType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleType cannot already have an ID")).body(null);
        }
        RuleType result = ruleTypeService.save(ruleType);
        return ResponseEntity.created(new URI("/api/rule-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-types : Updates an existing ruleType.
     *
     * @param ruleType the ruleType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleType,
     * or with status 400 (Bad Request) if the ruleType is not valid,
     * or with status 500 (Internal Server Error) if the ruleType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-types")
    @Timed
    public ResponseEntity<RuleType> updateRuleType(@Valid @RequestBody RuleType ruleType) throws URISyntaxException {
        log.debug("REST request to update RuleType : {}", ruleType);
        if (ruleType.getId() == null) {
            return createRuleType(ruleType);
        }
        RuleType result = ruleTypeService.save(ruleType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-types : get all the ruleTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ruleTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rule-types")
    @Timed
    public ResponseEntity<List<RuleType>> getAllRuleTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RuleTypes");
        Page<RuleType> page = ruleTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rule-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rule-types/:id : get the "id" ruleType.
     *
     * @param id the id of the ruleType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleType, or with status 404 (Not Found)
     */
    @GetMapping("/rule-types/{id}")
    @Timed
    public ResponseEntity<RuleType> getRuleType(@PathVariable Long id) {
        log.debug("REST request to get RuleType : {}", id);
        RuleType ruleType = ruleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleType));
    }

    /**
     * DELETE  /rule-types/:id : delete the "id" ruleType.
     *
     * @param id the id of the ruleType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleType(@PathVariable Long id) {
        log.debug("REST request to delete RuleType : {}", id);
        ruleTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
