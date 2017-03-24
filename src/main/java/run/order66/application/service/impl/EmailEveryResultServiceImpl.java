package run.order66.application.service.impl;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.executor.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("email_everyresult")
@Transactional
public class EmailEveryResultServiceImpl extends Executor {

    private final Logger log = LoggerFactory.getLogger(EmailEveryResultServiceImpl.class);
	@Override
	public RuleReport execute(Rule rule, RuleReport report) {
		log.info("Sending email for rule");
		// TODO
		return report;
	}

}
