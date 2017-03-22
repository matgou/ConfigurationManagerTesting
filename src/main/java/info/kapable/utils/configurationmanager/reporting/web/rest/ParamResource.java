package info.kapable.utils.configurationmanager.reporting.web.rest;

import com.codahale.metrics.annotation.Timed;
import info.kapable.utils.configurationmanager.reporting.domain.Param;
import info.kapable.utils.configurationmanager.reporting.service.ParamService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Param.
 */
@RestController
@RequestMapping("/api")
public class ParamResource {

    private final Logger log = LoggerFactory.getLogger(ParamResource.class);

    private static final String ENTITY_NAME = "param";
        
    private final ParamService paramService;

    public ParamResource(ParamService paramService) {
        this.paramService = paramService;
    }

    /**
     * POST  /params : Create a new param.
     *
     * @param param the param to create
     * @return the ResponseEntity with status 201 (Created) and with body the new param, or with status 400 (Bad Request) if the param has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/params")
    @Timed
    public ResponseEntity<Param> createParam(@Valid @RequestBody Param param) throws URISyntaxException {
        log.debug("REST request to save Param : {}", param);
        if (param.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new param cannot already have an ID")).body(null);
        }
        Param result = paramService.save(param);
        return ResponseEntity.created(new URI("/api/params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /params : Updates an existing param.
     *
     * @param param the param to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated param,
     * or with status 400 (Bad Request) if the param is not valid,
     * or with status 500 (Internal Server Error) if the param couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/params")
    @Timed
    public ResponseEntity<Param> updateParam(@Valid @RequestBody Param param) throws URISyntaxException {
        log.debug("REST request to update Param : {}", param);
        if (param.getId() == null) {
            return createParam(param);
        }
        Param result = paramService.save(param);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, param.getId().toString()))
            .body(result);
    }

    /**
     * GET  /params : get all the params.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of params in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/params")
    @Timed
    public ResponseEntity<List<Param>> getAllParams(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Params");
        Page<Param> page = paramService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/params");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /params/:id : get the "id" param.
     *
     * @param id the id of the param to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the param, or with status 404 (Not Found)
     */
    @GetMapping("/params/{id}")
    @Timed
    public ResponseEntity<Param> getParam(@PathVariable Long id) {
        log.debug("REST request to get Param : {}", id);
        Param param = paramService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(param));
    }

    /**
     * DELETE  /params/:id : delete the "id" param.
     *
     * @param id the id of the param to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/params/{id}")
    @Timed
    public ResponseEntity<Void> deleteParam(@PathVariable Long id) {
        log.debug("REST request to delete Param : {}", id);
        paramService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
