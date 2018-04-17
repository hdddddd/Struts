package actionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.FileType;
import util.FileTypeJudge;
import config.ActionConfig;



public class FileDownloadHandler {
	private Object action;
	private ActionConfig actionConfig;
	private ActionContext actionContext;

	public Object getAction() {
		return action;
	}

	public void setAction(Object action) {
		this.action = action;
	}

	public ActionConfig getActionConfig() {
		return actionConfig;
	}

	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	public ActionContext getActionContext() {
		return actionContext;
	}

	public void setActionContext(ActionContext actionContext) {
		this.actionContext = actionContext;
	}

	public FileDownloadHandler() {
		super();
	}

	public FileDownloadHandler(Object action, ActionConfig actionConfig, ActionContext actionContext) {
		super();
		this.action = action;
		this.actionConfig = actionConfig;
		this.actionContext = actionContext;
	}

	public void execute() {
		// 对文件下载进行处理
		executeDownloadFile();
	}

	public void executeDownloadFile() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		Map<String, String> map = actionConfig.getResults();
		Set<Entry<String, String>> set = map.entrySet();
		try {
			for (Entry<String, String> entry : set) {
				if (entry.getKey().equals("stream")) {
					// 如果是请求的下载文件操作
					String fileNameParam = actionContext.getRequest().getParameterNames().nextElement();
					Field field = action.getClass().getDeclaredField(fileNameParam);
					field.setAccessible(true);
					String fileName = (String) field.get(action);
					Document document  = DocumentHelper.parseText(entry.getValue());
					String xpath = "//param";

					List<Element> nodes = document.selectNodes(xpath);

					if (nodes != null && nodes.size() > 0) {
						for (Element element: nodes) {
							// 获取name属性的值
							String name = element.attributeValue("name");
							if (name.equals("inputName")) {
								String inputMethod = element.getText();
								inputMethod = "get" + inputMethod.substring(0, 1).toUpperCase() + inputMethod.substring(1);
								Method method2 = action.getClass().getMethod(inputMethod);
								inputStream = (InputStream) method2.invoke(action);
								inputStream.mark(0);
								inputStream = FileTypeJudge.getInputStream(inputStream);
								// 获取文件类型
								FileType fileType = FileTypeJudge.getType(inputStream);
								inputStream.reset();
								actionContext.getResponse().setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") +  "." + (fileType == null ? "txt" : fileType.toString()));
								outputStream = actionContext.getResponse().getOutputStream();
								byte[] bytes = new byte[1024];
								while ((inputStream.read(bytes)) != -1) {
									outputStream.write(bytes);
								}
							}
						}
						
						outputStream.flush();
					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (outputStream != null) {
				try {
					outputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
