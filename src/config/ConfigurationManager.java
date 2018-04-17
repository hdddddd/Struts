package config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
* @ClassName: ConfigurationManager 
* @Description: TODO(读取struts.xml) 
* @author (hedy)   
* @version V1.0
 */

public class ConfigurationManager {

	/*
	 * 读取struts.xml中传入的name变量的constant标签，返回符合条件的该标签的value属性值
	 */
	public static String getConstant(String name) {
		Document doc = getDocument();
		// XPath语句,选取属性name值为变量name的值的constant标签
		String xpath = "//constant[@name='" + name +"']";
		Element element = (Element) doc.selectSingleNode(xpath);

		if (element != null) {
			return element.attributeValue("value");
		}
		else {
			return null;
		}
	}

	/*
	 * 加载配置文件，返回配置文件对应的文档对象
	 */
	private static Document getDocument() {
		// 创建解析器
		SAXReader reader = new SAXReader();
		// 加载配置文件
		InputStream inputStream = ConfigurationManager.class.getResourceAsStream("/struts.xml");
		Document doc = null;

		try {
			doc = reader.read(inputStream);
		}
		catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("加载配置文件出错");
		}

		return doc;
	}

	/*
	 * 读取struts.xml中所有的interceptor标签信息，返回struts.xml中包含的所有interceptor标签的列表
	 */
	public static List<String> getInterceptors() {
		List<String> interceptors = null;
		Document document = getDocument();
		// XPath语句,选取所有的interceptor子元素,而不管他们在配置文件中的位置
		String xpath = "//interceptor";

		@SuppressWarnings("unchecked")
		List<Element> nodes = document.selectNodes(xpath);

		if (nodes != null && nodes.size() > 0) {
			interceptors = new ArrayList<>();
			for (Element element: nodes) {
				// 获取class属性的值
				String className = element.attributeValue("class");
				interceptors.add(className);
			}
		}

		return interceptors;
	}

	public static Map<String, String> getInterceptorParam(String className) {
		Map<String, String> resultMap = new HashMap<>();
		Document document = getDocument();
		// XPath语句,选取所有的interceptor子元素,而不管他们在配置文件中的位置
		String xpath = "//interceptor";

		@SuppressWarnings("unchecked")
		List<Element> nodes = document.selectNodes(xpath);
		if (nodes != null && nodes.size() > 0) {
			for (Element element: nodes) {
				if (className.equals(element.attributeValue("class"))) {
					@SuppressWarnings("unchecked")
					List<Element> paramElements = element.selectNodes("//param");
					
					for (Element paramEle : paramElements) {
						resultMap.put(paramEle.attributeValue("name"), paramEle.getText());
					}
				}
			}
			
			
		}
		
		return resultMap;
	}

	/*
	 * 读取struts.xml中所有action标签的信息
	 */
	public static Map<String, ActionConfig> getActions() {
		Map<String, ActionConfig> actionMap = null;
		Document document = getDocument();
		//XPath语句,读取配置文件中的action标签的信息,而无论他们在配置文件中的位子
		String xpath = "//action";

		@SuppressWarnings("unchecked")
		List<Element> nodes = document.selectNodes(xpath);

		if (nodes != null && nodes.size() > 0) {
			actionMap = new HashMap<>();
			for (Element element: nodes) {
				ActionConfig actionConfig = new ActionConfig();
				actionConfig.setName(element.attributeValue("name"));
				actionConfig.setClassName(element.attributeValue("class"));
				String method = element.attributeValue("method");
			
				method = (method == null || method.trim().equals("")) ? "execute" : method;
				actionConfig.setMethod(method);
				// 获取当前action标签下的所有result标签
				@SuppressWarnings("unchecked")
				List<Element> results = element.elements("result");

				for (Element result: results) {
					if (result.element("param") != null) {
						actionConfig.getResults().put(result.attributeValue("name"), result.asXML());
					}
					else {
						actionConfig.getResults().put(result.attributeValue("name"), result.getText());
					}
				}

				actionMap.put(actionConfig.getName(), actionConfig);
			}
		}

		return actionMap;
	}
}
