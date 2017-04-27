package run.order66.application.service.impl;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.enumeration.StatusEnum;
import run.order66.application.executor.Executor;
import run.order66.application.service.AsyncExecutorService;
import run.order66.application.service.BroadcastEventWebSocket;
import run.order66.application.service.RuleReportService;
import run.order66.application.service.RuleService;

import java.time.ZonedDateTime;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AsyncExecutorServiceImpl implements AsyncExecutorService {

    private final Logger log = LoggerFactory.getLogger(AsyncExecutorServiceImpl.class);
    
    @Autowired
    private RuleReportService ruleReportService;
    
    @Autowired
    private RuleService ruleService;

    @Autowired
    private ApplicationContext appContext;

	@Autowired
	BroadcastEventWebSocket broadcaster;
	
    @Async
    public Future<RuleReport> executeAsync(RuleReport report) {
		broadcaster.notifyRuleReport(report);
		
		Rule rule = report.getRule();
		rule.setDisplayStatus(StatusEnum.Running);
		ruleService.save(rule);
		
		Executor bean = (Executor) appContext.getBean(rule.getRuleType().getCheckerBeanName());
		report = bean.execute(rule, report);
		
		report.setFinishAt(ZonedDateTime.now());
		ruleReportService.save(report);

		String reportingBeanName = rule.getRuleType().getReportingBeanName();
		if(reportingBeanName != null) {
			Executor beanReporting = (Executor) appContext.getBean(rule.getRuleType().getReportingBeanName());
			beanReporting.execute(rule, report);
		}
		
		rule.setDisplayStatus(report.getStatus());
		ruleService.save(rule);
		broadcaster.notifyRuleReport(report);
		
        return new AsyncResult<>(report);
	}
}
