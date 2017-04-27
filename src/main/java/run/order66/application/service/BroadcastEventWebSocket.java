package run.order66.application.service;
import java.util.Observable;

import run.order66.application.domain.RuleReport;

public abstract class BroadcastEventWebSocket extends Observable {
	/**
	 * Notify all subobject that a ruleReport is update
	 * @param RuleReport
	 */
	public abstract void notifyRuleReport(RuleReport report);
}
