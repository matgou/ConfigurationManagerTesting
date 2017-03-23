package info.kapable.utils.configurationmanager.reporting.service;

import java.util.ArrayList;
import java.util.List;

import info.kapable.utils.configurationmanager.reporting.config.Constants;
import info.kapable.utils.configurationmanager.reporting.domain.Process;
import info.kapable.utils.configurationmanager.reporting.repository.ProcessRepository;
import info.kapable.utils.configurationmanager.reporting.service.dto.ProcessTreeDTO;
import info.kapable.utils.configurationmanager.reporting.service.dto.UserDTO;
import info.kapable.utils.configurationmanager.reporting.service.mapper.ProcessTreeMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing Process.
 */
@Service
@Transactional
public class ProcessService {

    private final Logger log = LoggerFactory.getLogger(ProcessService.class);
    
    private final ProcessRepository processRepository;

    @Autowired
    private RuleService ruleService;
    
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    /**
     * Save a process.
     *
     * @process process the entity to save
     * @return the persisted entity
     */
    public Process save(Process process) {
        log.debug("Request to save Process : {}", process);
        Process result = processRepository.save(process);
        return result;
    }

    /**
     *  Get all the processs.
     *  
     *  @process pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Process> findAll(Pageable pageable) {
        log.debug("Request to get all Processs");
        Page<Process> result = processRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one process by id.
     *
     *  @process id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Process findOne(Long id) {
        log.debug("Request to get Process : {}", id);
        Process process = processRepository.findOne(id);
        return process;
    }

    /**
     *  Delete the  process by id.
     *
     *  @process id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Process : {}", id);
        processRepository.delete(id);
    }

	public Page<ProcessTreeDTO> findAllRoot(Pageable pageable) {
		Page<ProcessTreeDTO> pList = processRepository.findAllRoot(pageable).map(ProcessTreeDTO::new);
		for(ProcessTreeDTO p: pList) {
			p.setChilds(this.findProcessTreeByParent(p.getId()));
			p.setRules(this.ruleService.findByProcess(p.getId()));
		}
		
		return pList;
	}

	private List<ProcessTreeDTO> findProcessTreeByParent(Long id) {
		List<ProcessTreeDTO> listProcessTreeDTO = new ArrayList<ProcessTreeDTO>();
		List<Process> pList = processRepository.findAllRoot(id);
		for(Process p: pList) {
			ProcessTreeDTO pTree = ProcessTreeMapper.INSTANCE.ProcessToProcessTreeDTO(p);
			pTree.setChilds(this.findProcessTreeByParent(p.getId()));
			pTree.setRules(this.ruleService.findByProcess(p.getId()));
			listProcessTreeDTO.add(pTree);
		}
		
		return listProcessTreeDTO;
	}
	
}
