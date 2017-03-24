package run.order66.application.service;

import run.order66.application.domain.Rule;
import run.order66.application.domain.Scheduling;
import run.order66.application.exception.UnsuportedTriggerException;

public interface SchedulingTaskService {
	public void createTrigger(Rule rule, Scheduling scheduling) throws UnsuportedTriggerException;
	
	public void unregisterJobFromRule(Rule rule);

	public void unregisterJobFromScheduling(Scheduling scheduling);
}
