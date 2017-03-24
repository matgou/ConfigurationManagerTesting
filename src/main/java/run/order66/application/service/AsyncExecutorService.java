package run.order66.application.service;

import java.util.concurrent.Future;

import run.order66.application.domain.RuleReport;

public interface AsyncExecutorService {

    public Future<RuleReport> executeAsync(RuleReport report);
}
