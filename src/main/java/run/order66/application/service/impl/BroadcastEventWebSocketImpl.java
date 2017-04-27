package run.order66.application.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import run.order66.application.domain.RuleReport;
import run.order66.application.service.BroadcastEventWebSocket;

@Service
public class BroadcastEventWebSocketImpl extends BroadcastEventWebSocket {

    private final Logger log = LoggerFactory.getLogger(BroadcastEventWebSocketImpl.class);
    
	@Override
	public void notifyRuleReport(RuleReport report) {
		log.error("notify observers that a report as changed");
		this.setChanged();
		this.notifyObservers(report);		
	}

}
