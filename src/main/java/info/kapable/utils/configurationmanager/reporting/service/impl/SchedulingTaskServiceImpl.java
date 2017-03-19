package info.kapable.utils.configurationmanager.reporting.service.impl;

import java.util.List;

import info.kapable.utils.configurationmanager.reporting.service.SchedulingService;
import info.kapable.utils.configurationmanager.reporting.service.SchedulingTaskService;
import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.domain.enumeration.TriggerEnum;
import info.kapable.utils.configurationmanager.reporting.exception.UnsuportedTriggerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import info.kapable.utils.configurationmanager.reporting.service.ExecutorTaskFactoryService;

import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;

@Service
@Transactional
public class SchedulingTaskServiceImpl implements SchedulingTaskService {

    private final Logger log = LoggerFactory.getLogger(SchedulingTaskServiceImpl.class);

    @Autowired
    private TaskScheduler scheduler;
    
    @Autowired
    private ExecutorTaskFactoryService executorTaskFactory;
    
    public void createTrigger(Rule rule, Scheduling scheduling) throws UnsuportedTriggerException {
    	switch(scheduling.getTrigger()) {
    		case cronSchedule:
    			createCronTrigger(rule, scheduling.getRule());
    			break;
    		default:
				throw new UnsuportedTriggerException();
    	}
    }


	private void createCronTrigger(Rule rule, String cronRule) {
		Trigger trigger = new CronTrigger(cronRule);
		Runnable task = executorTaskFactory.createTask(rule);
		scheduler.schedule(task, trigger);
	}
}
