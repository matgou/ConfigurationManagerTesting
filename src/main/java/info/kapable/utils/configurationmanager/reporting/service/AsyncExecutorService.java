package info.kapable.utils.configurationmanager.reporting.service;

import java.util.concurrent.Future;

import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;

public interface AsyncExecutorService {

    public Future<RuleReport> executeAsync(RuleReport report);
}
