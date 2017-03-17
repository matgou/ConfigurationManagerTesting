package info.kapable.utils.configurationmanager.reporting.web.rest;

import com.codahale.metrics.annotation.Timed;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.service.SchedulingService;
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
 * REST controller for managing Scheduling.
 */
@RestController
@RequestMapping("/api")
public class SchedulingResource {

    private final Logger log = LoggerFactory.getLogger(SchedulingResource.class);

    private static final String ENTITY_NAME = "scheduling";
        
    private final SchedulingService schedulingService;

    public SchedulingResource(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    /**
     * POST  /schedulings : Create a new scheduling.
     *
     * @param scheduling the scheduling to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduling, or with status 400 (Bad Request) if the scheduling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedulings")
    @Timed
    public ResponseEntity<Scheduling> createScheduling(@RequestBody Scheduling scheduling) throws URISyntaxException {
        log.debug("REST request to save Scheduling : {}", scheduling);
        if (scheduling.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new scheduling cannot already have an ID")).body(null);
        }
        Scheduling result = schedulingService.save(scheduling);
        return ResponseEntity.created(new URI("/api/schedulings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedulings : Updates an existing scheduling.
     *
     * @param scheduling the scheduling to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduling,
     * or with status 400 (Bad Request) if the scheduling is not valid,
     * or with status 500 (Internal Server Error) if the scheduling couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedulings")
    @Timed
    public ResponseEntity<Scheduling> updateScheduling(@RequestBody Scheduling scheduling) throws URISyntaxException {
        log.debug("REST request to update Scheduling : {}", scheduling);
        if (scheduling.getId() == null) {
            return createScheduling(scheduling);
        }
        Scheduling result = schedulingService.save(scheduling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduling.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedulings : get all the schedulings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedulings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedulings")
    @Timed
    public ResponseEntity<List<Scheduling>> getAllSchedulings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Schedulings");
        Page<Scheduling> page = schedulingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedulings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedulings/:id : get the "id" scheduling.
     *
     * @param id the id of the scheduling to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduling, or with status 404 (Not Found)
     */
    @GetMapping("/schedulings/{id}")
    @Timed
    public ResponseEntity<Scheduling> getScheduling(@PathVariable Long id) {
        log.debug("REST request to get Scheduling : {}", id);
        Scheduling scheduling = schedulingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(scheduling));
    }

    /**
     * DELETE  /schedulings/:id : delete the "id" scheduling.
     *
     * @param id the id of the scheduling to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedulings/{id}")
    @Timed
    public ResponseEntity<Void> deleteScheduling(@PathVariable Long id) {
        log.debug("REST request to delete Scheduling : {}", id);
        schedulingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
