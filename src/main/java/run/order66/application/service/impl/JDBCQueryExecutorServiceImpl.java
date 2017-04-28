package run.order66.application.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.enumeration.StatusEnum;
import run.order66.application.executor.Executor;
import run.order66.application.service.JDBCQueryExecutorService;
import run.order66.application.service.RuleReportService;

@Service("jdbc_query_executor")
@Transactional
/**
 * This class execute query and generate ruleReport for each line of query
 * @author MGOULIN
 *
 *
 *			String jdbcClass = props.get("jdbc_class");
 *			String jdbcUrl = props.get("jdbc_url");
 *			String sql = props.get("sql");
 *			String keyColumn = props.get("column_key");
 *			String finishAtColumn = props.get("column_time_start");
 *			String submitAtColumn = props.get("column_time_end");
 *			String statusColumn = props.get("column_status");
 *			String expectedStatus = props.get("column_expected_status");
 */
public class JDBCQueryExecutorServiceImpl extends Executor implements
JDBCQueryExecutorService {

	private final Logger log = LoggerFactory
			.getLogger(JDBCQueryExecutorServiceImpl.class);

	@Autowired
	private RuleReportService ruleReportService;

	/**
	 * Execute a JDBC query from args
	 * @param jdbcClass
	 * @param jdbcUrl
	 * @param sql
	 * @param username
	 * @param password
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private ResultSet ExecuteJDBCQuery(String jdbcClass, String jdbcUrl, String sql, String username, String password) throws ClassNotFoundException, SQLException {
		// load Driver
		Class.forName(jdbcClass);
		// Create the properties object
		Properties conProps = new Properties();
		conProps.setProperty("user",username);
		conProps.setProperty("password",password);
		
		// Connexion
		Connection con = DriverManager.getConnection(jdbcUrl, conProps);
		
		// Execute Query
		this.log.info("Execute : " + sql);
		Statement stmt = con.createStatement();
		ResultSet resultats = stmt.executeQuery(sql);
		return resultats;
	}

	@Override
	public RuleReport execute(Rule rule, RuleReport report) {
		String restultAsString = "";
		report.setRule(rule);
		int nbHardFail = 0;
		try {
			// Get properties from rule JSON
			Map<String, String> props = getPropertyFromJson(rule.getRuleArgs());
			String jdbcClass = props.get("jdbc_class");
			String jdbcUrl = props.get("jdbc_url");
			String sql = props.get("sql");
			String keyColumn = props.get("column_key");
			String username = props.get("username");
			String password = props.get("password");
			String finishAtColumn = props.get("column_time_end");
			String submitAtColumn = props.get("column_time_start");
			String statusColumn = props.get("column_status");
			int minCount = Integer.parseInt(props.get("minimum_count"));
			Pattern expectedStatus = Pattern.compile("^" + props.get("column_expected_status") + "$");

			// Execute query
			ResultSet resultats = ExecuteJDBCQuery(jdbcClass, jdbcUrl, sql, username, password);
			ResultSetMetaData rsmd = resultats.getMetaData();

			// For each result of query
			while (resultats.next()) {
				// Get RuleReport value from result 
				String key = resultats.getString(keyColumn);
				String log = to_json(resultats, rsmd);
				java.sql.Timestamp ts;
				ts = resultats.getTimestamp(finishAtColumn);
				ZonedDateTime finishAt= ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.of("UTC"));
				ts = resultats.getTimestamp(submitAtColumn);
				ZonedDateTime submitAt= ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.of("UTC"));

				// Test if OK
				String statusResultat = resultats.getString(statusColumn);
				Matcher matcher = expectedStatus.matcher(statusResultat);
				StatusEnum status;
				if(matcher.matches()) {
					status = StatusEnum.Success;
				} else {
					status = StatusEnum.HardFail;
					nbHardFail++;
					this.log.debug("status '" + statusResultat + "' not match + '^" + props.get("column_expected_status") + "$' json='" + log + "'");
					//log= "status '" + statusResultat + "' not match + '^" + props.get("column_expected_status") + "$' json='" + log + "'";
				}
				RuleReport r = createRuleReport(key, report, log, rule, submitAt, finishAt, status);
				report.addChilds(r);
			}

			// if no child with hardfail status => set Success:
			int realCount = report.getChilds().size();
			String logRuleReport = "";
			StatusEnum statusRuleReport = StatusEnum.Unknown;
			if(nbHardFail == 0) {
				if(realCount > minCount) {
					logRuleReport="OK (count = " +  report.getChilds().size() + " )";
					statusRuleReport = StatusEnum.Success;
				} else {
					logRuleReport = "No enough child (count = " +  report.getChilds().size() + " )" ;
					statusRuleReport = StatusEnum.HardFail;	
				}
			} else {
				logRuleReport = "Some HardFail detected in child Report (hardfail : " + nbHardFail + " / " + realCount + ")";
				statusRuleReport = StatusEnum.HardFail;
			}
			report.setLog(logRuleReport);
			report.setStatus(statusRuleReport);
		} catch (ClassNotFoundException e) {
			log.error(
					"an exception was thrown in JDBCQueryExecutorServiceImpl",
					e);
			report.setLog("ClassNotFoundException : " + e.getMessage());
			report.setStatus(StatusEnum.SoftFail);

		} catch (IOException e) {
			log.error(
					"an exception was thrown in JDBCQueryExecutorServiceImpl",
					e);
			report.setLog(restultAsString);
			report.setStatus(StatusEnum.SoftFail);
		} catch (SQLException e) {
			log.error(
					"an exception was thrown in JDBCQueryExecutorServiceImpl",
					e);
			report.setLog("SQLException : " + e.getMessage());
			report.setStatus(StatusEnum.SoftFail);
		} catch (JSONException e) {
			log.error(
					"an exception was thrown in JDBCQueryExecutorServiceImpl",
					e);
			report.setLog("JSONException : " + e.getMessage());
			report.setStatus(StatusEnum.SoftFail);
		}

		return report;
	}

	/**
	 * Create a new Child report from args
	 * 
	 * @param key
	 * @param parentReport
	 * @param log
	 * @param rule
	 * @param submitAt
	 * @param finishAt
	 * @param status
	 * @return
	 */
	private RuleReport createRuleReport(String key, RuleReport parentReport, String log, Rule rule, ZonedDateTime submitAt, ZonedDateTime finishAt, StatusEnum status) {
		RuleReport report = new RuleReport();
		report.setKEY(key);
		report.setLog(log);
		report.setRule(rule);
		report.setSubmitAt(submitAt);
		report.setStatus(status);
		report.setFinishAt(finishAt);
		report.setParent(parentReport);
		this.ruleReportService.save(report);
		return report;
	}

	/**
	 * Convert a resultset to JSON
	 * @param rs
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	private String to_json(ResultSet rs, ResultSetMetaData rsmd) throws SQLException, JSONException {

		int numColumns = rsmd.getColumnCount();
		JSONObject obj = new JSONObject();

		for (int i = 1; i < numColumns + 1; i++) {
			String column_name = rsmd.getColumnName(i);

			switch (rsmd.getColumnType(i)) {
			case java.sql.Types.ARRAY:
				obj.put(column_name, rs.getArray(column_name));
				break;
			case java.sql.Types.BIGINT:
				obj.put(column_name, rs.getInt(column_name));
				break;
			case java.sql.Types.BOOLEAN:
				obj.put(column_name, rs.getBoolean(column_name));
				break;
			case java.sql.Types.BLOB:
				obj.put(column_name, rs.getBlob(column_name));
				break;
			case java.sql.Types.DOUBLE:
				obj.put(column_name, rs.getDouble(column_name));
				break;
			case java.sql.Types.FLOAT:
				obj.put(column_name, rs.getFloat(column_name));
				break;
			case java.sql.Types.INTEGER:
				obj.put(column_name, rs.getInt(column_name));
				break;
			case java.sql.Types.NVARCHAR:
				obj.put(column_name, rs.getNString(column_name));
				break;
			case java.sql.Types.VARCHAR:
				obj.put(column_name, rs.getString(column_name));
				break;
			case java.sql.Types.TINYINT:
				obj.put(column_name, rs.getInt(column_name));
				break;
			case java.sql.Types.SMALLINT:
				obj.put(column_name, rs.getInt(column_name));
				break;
			case java.sql.Types.DATE:
				obj.put(column_name, rs.getDate(column_name));
				break;
			case java.sql.Types.TIMESTAMP:
				obj.put(column_name, rs.getTimestamp(column_name));
				break;
			default:
				obj.put(column_name, rs.getObject(column_name));
				break;
			}
		}
		return obj.toString();
	}
}
