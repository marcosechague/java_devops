package util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.log4j.Logger;

public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class);
	
	private Utils() {}
	
	public static LinkedList<Map<String, String>> getNavigators(String value) throws Exception {
	
		Map<String, Map<String, Object>> fileMap = castYAML();
		
		LinkedList<Map<String, String>> browsers = new LinkedList<Map<String, String>>();
		
		logger.info("The tests will run in the following environments");
		
		for (String plataforma : fileMap.keySet()) {
			Map<String, String> caps = new HashMap<String, String>();
			for (String key : fileMap.get(plataforma).keySet()) {
				caps.put(key, fileMap.get(plataforma).get(key).toString());
			}
			
			if((!value.equals("ALL") && plataforma.equals(value)) || (value.equals("ALL"))) {
				browsers.add(caps);
				logger.info(plataforma);
			}
		}

		if(browsers.size() == 0 && Constants.ENVIROMENT.equals(Constants.LOCAL_ENVIROMENT)) {
			Map<String, String> caps = new HashMap<String, String>();
			caps.put("platform", Constants.OS_NAME);
			caps.put("browserName", Constants.BROWSER);
			caps.put("browserVersion", "");
			browsers.add(caps);
			logger.info(Constants.ENVIROMENT + " -- " + Constants.BROWSER);
		}
		
		return browsers;
	}
	
	private static Map<String, Map<String, Object>> castYAML() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		
	    Map<String, Map<String, Object>> fileMap = new HashMap<String, Map<String,Object>>();
	    
	    logger.info("Reading platforms.yml file");
	    
		try {
			fileMap = mapper.readValue(
			    new File("src/test/resources/platforms/platforms.yml"), 
			    new TypeReference<Map<String, Map<String, Object>>>(){});
		} catch (JsonParseException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		} catch (JsonMappingException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		} catch (IOException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
	    
	    return fileMap;
	}	
}
