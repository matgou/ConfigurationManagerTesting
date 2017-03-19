package info.kapable.utils.configurationmanager.reporting.service;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.exception.UnsuportedTriggerException;

public interface SchedulingTaskService {
	public void createTrigger(Rule rule, Scheduling scheduling) throws UnsuportedTriggerException;
}
