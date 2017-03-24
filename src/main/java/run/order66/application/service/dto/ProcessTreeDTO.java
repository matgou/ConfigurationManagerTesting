package run.order66.application.service.dto;

import java.util.List;

import run.order66.application.domain.Rule;
import run.order66.application.domain.Process;

public class ProcessTreeDTO {

    private Long id;
    
    private String processName;
    
    private List<ProcessTreeDTO> childs;
    
    private List<Rule> rules;


    public ProcessTreeDTO() {
        // Empty constructor needed for MapStruct.
    }

    public ProcessTreeDTO(Process process) {
        this.setProcessName(process.getProcessName());
        this.setId(process.getId());
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public List<ProcessTreeDTO> getChilds() {
		return childs;
	}

	public void setChilds(List<ProcessTreeDTO> childs) {
		this.childs = childs;
	}
}
