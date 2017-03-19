package info.kapable.utils.configurationmanager.reporting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.exception.UnsuportedTriggerException;
import info.kapable.utils.configurationmanager.reporting.executor.RunnableTaskExecutor;

@Service
@Transactional
public class ExecutorTaskFactoryService implements InitializingBean {
	
	@Autowired
	private AsyncExecutorService asyncExecutor;
	@Autowired 
	private RuleReportService ruleReportService;
	@Autowired 
	private RuleService ruleService;
	@Autowired
	private SchedulingService schedulingService;
	@Autowired
	private SchedulingTaskService schedulingTaskService;
	
    private final Logger log = LoggerFactory.getLogger(ExecutorTaskFactoryService.class);

    public void afterPropertiesSet() {
    	log.info("Executing : afterPropertiesSet on SchedulingTaskServiceImpl");
    	for(Scheduling scheduling: schedulingService.findAllFetchRules()) {
    		for(Rule rule: scheduling.getRules()) {
    			try {
    				schedulingTaskService.createTrigger(rule, scheduling);
				} catch (UnsuportedTriggerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
	public Runnable createTask(Rule rule) {
		// TODO Auto-generated method stub
		return new RunnableTaskExecutor(rule, asyncExecutor, ruleReportService, ruleService);
	}

}
