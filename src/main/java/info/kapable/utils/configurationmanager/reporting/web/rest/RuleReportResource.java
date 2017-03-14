package info.kapable.utils.configurationmanager.reporting.web.rest;

import com.codahale.metrics.annotation.Timed;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.service.RuleReportService;
import info.kapable.utils.configurationmanager.reporting.web.rest.util.HeaderUtil;
import info.kapable.utils.configurationmanager.reporting.web.rest.util.PaginationUtil;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RuleReport.
 */
@RestController
@RequestMapping("/api")
public class RuleReportResource {

    private final Logger log = LoggerFactory.getLogger(RuleReportResource.class);

    private static final String ENTITY_NAME = "ruleReport";
        
    private final RuleReportService ruleReportService;

    public RuleReportResource(RuleReportService ruleReportService) {
        this.ruleReportService = ruleReportService;
    }

    /**
     * POST  /rule-reports : Create a new ruleReport.
     *
     * @param ruleReport the ruleReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleReport, or with status 400 (Bad Request) if the ruleReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-reports")
    @Timed
    public ResponseEntity<RuleReport> createRuleReport(@RequestBody RuleReport ruleReport) throws URISyntaxException {
        log.debug("REST request to save RuleReport : {}", ruleReport);
        if (ruleReport.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleReport cannot already have an ID")).body(null);
        }
        RuleReport result = ruleReportService.save(ruleReport);
        return ResponseEntity.created(new URI("/api/rule-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-reports : Updates an existing ruleReport.
     *
     * @param ruleReport the ruleReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleReport,
     * or with status 400 (Bad Request) if the ruleReport is not valid,
     * or with status 500 (Internal Server Error) if the ruleReport couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-reports")
    @Timed
    public ResponseEntity<RuleReport> updateRuleReport(@RequestBody RuleReport ruleReport) throws URISyntaxException {
        log.debug("REST request to update RuleReport : {}", ruleReport);
        if (ruleReport.getId() == null) {
            return createRuleReport(ruleReport);
        }
        RuleReport result = ruleReportService.save(ruleReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-reports : get all the ruleReports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ruleReports in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rule-reports")
    @Timed
    public ResponseEntity<List<RuleReport>> getAllRuleReports(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RuleReports");
        Page<RuleReport> page = ruleReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rule-reports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rule-reports/:id : get the "id" ruleReport.
     *
     * @param id the id of the ruleReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleReport, or with status 404 (Not Found)
     */
    @GetMapping("/rule-reports/{id}")
    @Timed
    public ResponseEntity<RuleReport> getRuleReport(@PathVariable Long id) {
        log.debug("REST request to get RuleReport : {}", id);
        RuleReport ruleReport = ruleReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleReport));
    }

    /**
     * DELETE  /rule-reports/:id : delete the "id" ruleReport.
     *
     * @param id the id of the ruleReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleReport(@PathVariable Long id) {
        log.debug("REST request to delete RuleReport : {}", id);
        ruleReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
