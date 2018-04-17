package config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
* @ClassName: ActionConfig 
* @Description: TODO(映射struts.xml中action标签的实体类) 
* @author (hedy)  
* @version V1.0
 */
public class ActionConfig {
	private String name;
	private String method;
	private String className;
	private Map<String, String> results = new LinkedHashMap<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public Map<String, String> getResults() {
		return results;
	}
	
	public void setResults(Map<String, String> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return "ActionConfig[name = " + name + ", method = " + method + ", className = " + className
				+ ", results = " + results + "]";
	}
}
