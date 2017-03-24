package run.order66.application.executor;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;

public abstract class Executor {

	public abstract RuleReport execute(Rule rule, RuleReport report);

}
