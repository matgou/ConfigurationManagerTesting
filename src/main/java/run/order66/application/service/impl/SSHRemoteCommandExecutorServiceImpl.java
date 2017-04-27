package run.order66.application.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.enumeration.StatusEnum;
import run.order66.application.executor.Executor;
import run.order66.application.service.SSHRemoteCommandExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service("ssh_remote_executor")
@Transactional
public class SSHRemoteCommandExecutorServiceImpl extends Executor implements
		SSHRemoteCommandExecutorService {

	private final Logger log = LoggerFactory
			.getLogger(SSHRemoteCommandExecutorServiceImpl.class);

	
	private RuleReport executeSSH(Map<String, String> props, Rule rule, RuleReport rValue) throws JSchException, IOException {
		int returnCode;
		String output;
		
		JSch jsch = new JSch();
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		
		Session session = jsch.getSession(props.get("user"), props.get("host"),
				22);
		session.setPassword(props.get("password"));
		session.setConfig(config);
		session.connect();
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel)
				.setCommand(props.get("cmd"));

		channel.connect();

		InputStream in = channel.getInputStream();
		BufferedReader fromChannel = new BufferedReader(new InputStreamReader(in, "UTF-8"));	
		((ChannelExec) channel).setErrStream(System.err);
		channel.connect();

        StringBuilder result = new StringBuilder();
        while(true) {
        	String line = fromChannel.readLine();
        	if(line != null) {
        		result.append(line);
        		result.append("\n");
        	}
			if (channel.isClosed()) {
				returnCode = channel.getExitStatus();
				break;
			}
         }

     	output = result.toString();
     	
		channel.disconnect();
		session.disconnect();
		
		StatusEnum status;
		switch (returnCode) {
			case 0:
				status = StatusEnum.Success;
				break;
			default:
				status = StatusEnum.HardFail;
				break;
		}
		rValue.setLog(output);
		rValue.setStatus(status);
		rValue.setRule(rule);
		return rValue;
	}

	@Override
	public RuleReport execute(Rule rule, RuleReport report) {
		log.info("Start executor for rule : " + rule.getId());
		try {
			Map<String, String> props = getPropertyFromJson(rule.getRuleArgs());
			return executeSSH(props, rule, report);
		} catch (JsonParseException e) {
			log.error("Probleme dans la saisie du champ arguements dans la rule " + rule.getId(), e);
			report.setLog("Probleme dans la saisie du champ arguements dans la rule " + rule.getId() + " : " + e.getMessage());
		} catch (JsonMappingException e) {
			log.error("Probleme dans la saisie du champ arguements dans la rule " + rule.getId(), e);
			report.log("Probleme dans la saisie du champ arguements dans la rule " + rule.getId() + " " + e.getMessage());
		} catch (IOException e) {
			log.error("Probleme dans la saisie du champ arguements dans la rule " + rule.getId(), e);
			report.log("Probleme dans la saisie du champ arguements dans la rule " + rule.getId() + " " + e.getMessage());
		} catch (JSchException e) {
			log.error("Probleme dans l'execution ssh "+ rule.getId(), e);
			report.log("Probleme dans l'execution ssh "+ rule.getId() + " " + e.getMessage());
		}
		return this.getSoftFailReport(rule, report);
	}

	private RuleReport getSoftFailReport(Rule rule, RuleReport rValue) {
		rValue.setRule(rule);
		rValue.setStatus(StatusEnum.SoftFail);
		return rValue;
	}

}
