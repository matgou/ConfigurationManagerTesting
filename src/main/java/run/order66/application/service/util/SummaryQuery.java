package run.order66.application.service.util;

import java.io.Serializable;
import java.util.Map;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleTag;
import run.order66.application.domain.enumeration.StatusEnum;

public class SummaryQuery implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String displayStatusCSV;
	private String tagsCSV;
	
	private Map<String,Boolean> displayStatus;

	/**
	 * @return the displayStatus
	 */
	public Map<String,Boolean> getDisplayStatus() {
		return displayStatus;
	}

	public boolean testRule(Rule rule) {
		for(RuleTag tag: rule.getTags()) {
			if(tagsCSV.contains(tag.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the tagsCSV
	 */
	public String getTagsCSV() {
		return tagsCSV;
	}

	/**
	 * @param tagsCSV the tagsCSV to set
	 */
	public void setTagsCSV(String tagsCSV) {
		this.tagsCSV = tagsCSV;
	}

	/**
	 * @return the displayStatusCSV
	 */
	public String getDisplayStatusCSV() {
		return displayStatusCSV;
	}

	/**
	 * @param displayStatusCSV the displayStatusCSV to set
	 */
	public void setDisplayStatusCSV(String displayStatusCSV) {
		this.displayStatusCSV = displayStatusCSV;
	}
}
