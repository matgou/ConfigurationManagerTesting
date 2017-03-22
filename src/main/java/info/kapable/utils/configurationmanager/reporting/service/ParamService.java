package info.kapable.utils.configurationmanager.reporting.service;

import info.kapable.utils.configurationmanager.reporting.domain.Param;
import info.kapable.utils.configurationmanager.reporting.repository.ParamRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Param.
 */
@Service
@Transactional
public class ParamService {

    private final Logger log = LoggerFactory.getLogger(ParamService.class);
    
    private final ParamRepository paramRepository;

    public ParamService(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    /**
     * Save a param.
     *
     * @param param the entity to save
     * @return the persisted entity
     */
    public Param save(Param param) {
        log.debug("Request to save Param : {}", param);
        Param result = paramRepository.save(param);
        return result;
    }

    /**
     *  Get all the params.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Param> findAll(Pageable pageable) {
        log.debug("Request to get all Params");
        Page<Param> result = paramRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one param by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Param findOne(Long id) {
        log.debug("Request to get Param : {}", id);
        Param param = paramRepository.findOne(id);
        return param;
    }

    /**
     *  Delete the  param by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Param : {}", id);
        paramRepository.delete(id);
    }

	public String getValue(String key) {
		Param param = this.paramRepository.findOneByKey(key);
		// TODO Auto-generated method stub
		return param.getValue();
	}
}
