package info.kapable.utils.configurationmanager.reporting.service.impl;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.executor.Executor;
import info.kapable.utils.configurationmanager.reporting.service.AsyncExecutorService;
import info.kapable.utils.configurationmanager.reporting.service.RuleReportService;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext appContext;
    
    @Async
    public Future<RuleReport> executeAsync(RuleReport report) {
		// TODO Auto-generated method stub
		Rule rule = report.getRule();
		Executor bean = (Executor) appContext.getBean(rule.getRuleType().getCheckerBeanName());
		report = bean.execute(rule, report);
		ruleReportService.save(report);
        return new AsyncResult<>(report);
	}
}
