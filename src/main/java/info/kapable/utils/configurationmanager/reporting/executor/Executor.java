package info.kapable.utils.configurationmanager.reporting.executor;

import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;

public abstract class Executor {

	public abstract RuleReport execute(Rule rule, RuleReport report);

}
