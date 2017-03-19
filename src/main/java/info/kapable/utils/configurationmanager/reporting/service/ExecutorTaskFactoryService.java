package info.kapable.utils.configurationmanager.reporting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.executor.RunnableTaskExecutor;

@Service
@Transactional
public class ExecutorTaskFactoryService {
	
	@Autowired
	private AsyncExecutorService asyncExecutor;
	@Autowired 
	private RuleReportService ruleReportService;
	@Autowired 
	private RuleService ruleService;

    private final Logger log = LoggerFactory.getLogger(ExecutorTaskFactoryService.class);

	public Runnable createTask(Rule rule) {
		// TODO Auto-generated method stub
		return new RunnableTaskExecutor(rule, asyncExecutor, ruleReportService, ruleService);
	}

}
