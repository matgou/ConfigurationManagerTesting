package run.order66.application.executor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import run.order66.application.domain.Rule;
import run.order66.application.domain.RuleReport;

public abstract class Executor {

	public abstract RuleReport execute(Rule rule, RuleReport report);

	protected Map<String, String> getPropertyFromJson(String jsonData) throws JsonParseException, JsonMappingException, IOException {

		TypeFactory factory = TypeFactory.defaultInstance();
		MapType type = factory.constructMapType(HashMap.class, String.class,
				String.class);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonData, type);
	}
}
