package info.kapable.utils.configurationmanager.reporting.service.impl;

import java.util.List;

import info.kapable.utils.configurationmanager.reporting.service.SchedulingService;
import info.kapable.utils.configurationmanager.reporting.service.SchedulingTaskService;
import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.domain.enumeration.TriggerEnum;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchedulingTaskServiceImpl implements SchedulingTaskService {

    private final Logger log = LoggerFactory.getLogger(SchedulingTaskServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    
    private SchedulingService schedulingDomainService;
    
    
    public void updateTrigger() {
    	List<Scheduling> schedulings = schedulingDomainService.findAll();
    	Scheduler scheduler = (Scheduler) schedulerFactory.getScheduler();
    	
    	for(Scheduling scheduling: schedulings) {
    		for(Rule rule: scheduling.getRules()) {
    			Trigger trigger;
    			if(scheduling.getTrigger() == TriggerEnum.cronSchedule) {
    				trigger = new CronTriggerBean();
    				trigger.setCronExpression(expression);
    				trigger.afterPropertiesSet();
    			}
                scheduler.scheduleJob((JobDetail) jobDetail.getObject(), trigger);
    		}
    	}
    }
}
