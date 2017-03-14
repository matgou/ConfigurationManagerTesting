package info.kapable.utils.configurationmanager.reporting.service.impl;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.executor.Executor;
import info.kapable.utils.configurationmanager.reporting.service.SSHRemoteCommandExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SSHRemoteCommandExecutorServiceImpl extends Executor implements SSHRemoteCommandExecutorService {

    private final Logger log = LoggerFactory.getLogger(SSHRemoteCommandExecutorServiceImpl.class);

	@Override
	public RuleReport execute(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

}
