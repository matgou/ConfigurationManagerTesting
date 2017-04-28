package run.order66.application.web.rest;

import com.codahale.metrics.annotation.Timed;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.RuleTag;
import run.order66.application.domain.enumeration.StatusEnum;
import run.order66.application.executor.Executor;
import run.order66.application.security.SecurityUtils;
import run.order66.application.service.AsyncExecutorService;
import run.order66.application.service.RuleReportService;
import run.order66.application.service.RuleService;
import run.order66.application.service.RuleTagService;
import run.order66.application.service.UserService;
import run.order66.application.service.mapper.RuleLastReportMapper;
import run.order66.application.web.rest.util.HeaderUtil;
import run.order66.application.web.rest.util.PaginationUtil;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import javax.validation.Valid;

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
	private AsyncExecutorService asyncExecutor;
    @Autowired
    private UserService userService;
    @Autowired
    private RuleTagService ruleTagService;
    
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
     * POST  /rule-tags : Create a new ruleTag.
     *
     * @param ruleTag the ruleTag to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleTag, or with status 400 (Bad Request) if the ruleTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rules/{id}/tags")
    @Timed
    public ResponseEntity<RuleTag> createRuleTag(@PathVariable Long id, @Valid @RequestBody RuleTag ruleTag) throws URISyntaxException {
        log.debug("REST request to add tag on Rule : {}", id);
        Rule rule = ruleService.findOne(id);
        if(rule == null) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (ruleTag.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleTag cannot already have an ID")).body(null);
        }
        ruleTag.setRule(rule);
        RuleTag result = ruleTagService.save(ruleTag);
        return ResponseEntity.created(new URI("/api/rule-tags/" + result.getId()))
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

        Calendar cal = Calendar.getInstance();
        LocalDate now = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        RuleReport report = new RuleReport();
        report.setStatus(StatusEnum.Running);
        report.setReportDate(now);
        report.setRule(rule);
    	report.setSubmitAt(ZonedDateTime.now());
    	report.setUser(userService.getUserWithAuthorities());
        if(report != null) {
        	ruleReportService.save(report);
        	rule.setLastReport(report);
        }
        rule.setDisplayStatus(StatusEnum.Running);
        ruleService.save(rule);
        
        Future<RuleReport> reportAsync = this.asyncExecutor.executeAsync(report);
        
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }


    /**
     * GET  /rules/:id/lastReport : get the last report of "id" rule.
     *
     * @param id the id of the lastReport rule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rule, or with status 404 (Not Found)
     */
    @GetMapping("/rules/{id}/lastReport")
    @Timed
    public ResponseEntity<RuleReport> getRuleLastReport(@PathVariable Long id) {
        log.debug("REST request to get Rule : {}", id);
        Rule rule = ruleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rule.getLastReport()));
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
