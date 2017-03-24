package run.order66.application.service.impl;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.executor.Executor;
import run.order66.application.service.ParamService;

@Service("time_influxdb_reporting")
@Transactional
public class DurationInfluxDBReportingImpl extends Executor {

    private final Logger log = LoggerFactory.getLogger(DurationInfluxDBReportingImpl.class);
	@Autowired
	ParamService paramService;
	
	static long dateTimeDifference(Temporal d1, Temporal d2, ChronoUnit unit){
	    return unit.between(d1, d2);
	}
	
	@Override
	public RuleReport execute(Rule rule, RuleReport report) {
		log.info("Sending duration to influxDB");
		
		long timeToSend = dateTimeDifference(report.getFinishAt(), report.getSubmitAt(), ChronoUnit.SECONDS);
		
		/* New connexion to influxdb */
		String influxdb_host = paramService.getValue("influxdb_host");
		String influxdb_user = paramService.getValue("influxdb_user");
		String influxdb_password = paramService.getValue("influxdb_password");
		String influxdb_dbname = paramService.getValue("influxdb_dbname");
		String influxdb_durationmetricname = paramService.getValue("influxdb_durationmetricname");
		InfluxDB influxDB = InfluxDBFactory.connect(influxdb_host, influxdb_user, influxdb_password);
		
		/* Send point to influxdb */
		Builder point = Point.measurement(influxdb_durationmetricname)
				.time(report.getSubmitAt().toEpochSecond(), TimeUnit.SECONDS);
	
		/* Add metadata to point */
		point = point.addField("value", timeToSend);
		point = point.addField("rule", rule.getRuleName());
		point = point.addField("state", report.getStatus().toString());
		
		/* If rule contains metadata add to point*/
		for(String arg: rule.getReportingArgs().split(",")) {
			String part[] = arg.split("=");
			if(part.length > 1) {
				point = point.addField(part[0], part[1]);
			}
		}
		
		/* write point */
		influxDB.write(influxdb_dbname, "autogen", point.build());
		influxDB.close();
		
		/* return report */
		return report;
	}
}
