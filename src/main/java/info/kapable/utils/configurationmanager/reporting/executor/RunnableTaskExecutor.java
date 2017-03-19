package info.kapable.utils.configurationmanager.reporting.executor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.Future;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.domain.enumeration.StatusEnum;
import info.kapable.utils.configurationmanager.reporting.service.AsyncExecutorService;
import info.kapable.utils.configurationmanager.reporting.service.RuleReportService;
import info.kapable.utils.configurationmanager.reporting.service.RuleService;

public class RunnableTaskExecutor implements Runnable {

	private Rule rule;
	private AsyncExecutorService asyncExecutor; 
	private RuleReportService ruleReportService; 
	private RuleService ruleService;
	
	public RunnableTaskExecutor(Rule rule, AsyncExecutorService asyncExecutor, RuleReportService ruleReportService, RuleService ruleService) {
		this.rule = rule;
		this.asyncExecutor = asyncExecutor;
		this.ruleReportService = ruleReportService;
		this.ruleService = ruleService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        Calendar cal = Calendar.getInstance();
        LocalDate now = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        RuleReport report = new RuleReport();
        report.setStatus(StatusEnum.Running);
        report.setReportDate(now);
        report.setRule(rule);
    	report.setSubmitAt(ZonedDateTime.now());

        if(report != null) {
        	ruleReportService.save(report);
        }
        rule.setDisplayStatus(StatusEnum.Running);
        ruleService.save(rule);
        
        Future<RuleReport> reportAsync = this.asyncExecutor.executeAsync(report);

	}

}
