package run.order66.application.executor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.Future;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.enumeration.StatusEnum;
import run.order66.application.service.AsyncExecutorService;
import run.order66.application.service.RuleReportService;
import run.order66.application.service.RuleService;
import run.order66.application.service.mapper.RuleLastReportMapper;

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
        	rule.setLastReport(RuleLastReportMapper.INSTANCE.ruleReportToRuleLastReportDTO(report));
        }
        rule.setDisplayStatus(StatusEnum.Running);
        ruleService.save(rule);
        
        Future<RuleReport> reportAsync = this.asyncExecutor.executeAsync(report);

	}

}
