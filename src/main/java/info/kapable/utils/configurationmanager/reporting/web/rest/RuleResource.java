package info.kapable.utils.configurationmanager.reporting.web.rest;

import com.codahale.metrics.annotation.Timed;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.executor.Executor;
import info.kapable.utils.configurationmanager.reporting.service.RuleReportService;
import info.kapable.utils.configurationmanager.reporting.service.RuleService;
import info.kapable.utils.configurationmanager.reporting.web.rest.util.HeaderUtil;
import info.kapable.utils.configurationmanager.reporting.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rule.
 */
@RestController
@RequestMapping("/api")
public class RuleResource {

    private final Logger log = LoggerFactory.getLogger(RuleResource.class);

    private static final String ENTITY_NAME = "rule";
        
    private final RuleService ruleService;
    @Autowired
    private RuleReportService ruleReportService;
    @Autowired
    private ApplicationContext appContext;
    
    public RuleResource(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * POST  /rules : Create a new rule.
     *
     * @param rule the rule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rule, or with status 400 (Bad Request) if the rule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rules")
    @Timed
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) throws URISyntaxException {
        log.debug("REST request to save Rule : {}", rule);
        if (rule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rule cannot already have an ID")).body(null);
        }
        Rule result = ruleService.save(rule);
        return ResponseEntity.created(new URI("/api/rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rules : Updates an existing rule.
     *
     * @param rule the rule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rule,
     * or with status 400 (Bad Request) if the rule is not valid,
     * or with status 500 (Internal Server Error) if the rule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rules")
    @Timed
    public ResponseEntity<Rule> updateRule(@RequestBody Rule rule) throws URISyntaxException {
        log.debug("REST request to update Rule : {}", rule);
        if (rule.getId() == null) {
            return createRule(rule);
        }
        Rule result = ruleService.save(rule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rules : get all the rules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rules")
    @Timed
    public ResponseEntity<List<Rule>> getAllRules(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Rules");
        Page<Rule> page = ruleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * POST  /rules/{id}/execute : Execute a rule.
     *
     * @param rule the rule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rule, or with status 400 (Bad Request) if the rule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rules/{id}/execute")
    @Timed
    public ResponseEntity<RuleReport> executeRule(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to execute Rule : {}", id);
        Rule rule = ruleService.findOne(id);
        if(rule == null) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String executor = rule.getRuleType().getCheckerBeanName();
        Executor executorBean = (Executor) appContext.getBean(executor);
        RuleReport report = executorBean.execute(rule);
        if(report != null) {
        	ruleReportService.save(report);
        }
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }

    /**
     * GET  /rules/:id : get the "id" rule.
     *
     * @param id the id of the rule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rule, or with status 404 (Not Found)
     */
    @GetMapping("/rules/{id}")
    @Timed
    public ResponseEntity<Rule> getRule(@PathVariable Long id) {
        log.debug("REST request to get Rule : {}", id);
        Rule rule = ruleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rule));
    }

    /**
     * DELETE  /rules/:id : delete the "id" rule.
     *
     * @param id the id of the rule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rules/{id}")
    @Timed
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        log.debug("REST request to delete Rule : {}", id);
        ruleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
