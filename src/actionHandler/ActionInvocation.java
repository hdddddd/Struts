package actionHandler;

import interceptor.Interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.ActionConfig;



/**
 * 
* @ClassName: ActionInvocation 
* @Description: TODO拦截器链和action的调用) 
* @author (hedy)  
* @version V1.0
 */
public class ActionInvocation {
	/*
	 * 拦截器链
	 */
	private Iterator<Interceptor> interceptors;
	/*
	 * 即将被调用的action实例
	 */
	private Object action;
	/*
	 * action配置信息
	 */
	private ActionConfig config;
	/*
	 * 本次请求的数据中心actionContext
	 */
	private ActionContext invocationContext;
	
	private FileDownloadHandler actionHandler;
	
	public ActionInvocation(List<String> interceptorClassNames, ActionConfig config, 
			HttpServletRequest request, HttpServletResponse response) {
		// 准备Interceptor链
		if (interceptorClassNames != null && interceptorClassNames.size() > 0) {
			List<Interceptor> list = new ArrayList<>();
			for (String className: interceptorClassNames) {
				try {
					Interceptor interceptor = (Interceptor) Class.forName(className).newInstance();
					interceptor.init();
					list.add(interceptor);
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("创建interceptor失败: " + className);
				}
			}
			interceptors = list.iterator();
		}
		
		// 准备action实例
		this.config = config;
		try {
			action = Class.forName(config.getClassName()).newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		invocationContext = new ActionContext(request, response, action);
		
		actionHandler = new FileDownloadHandler(this.action, this.config, this.invocationContext);
	}
	
	/*
	 * 调用拦截器链和action处理方法
	 */
	public String invoke(ActionInvocation invocation) {
		// 结果串
		String result = null;
		// 判断拦截器链是否还有下一个拦截器或者未被拦截器拦截(result不等于null的话说明已经有拦截器返回结果串了)
		if (interceptors != null && interceptors.hasNext() && result == null) {
			//有下一个拦截器，调用下一个拦截器
			Interceptor next = interceptors.next();
			result = next.intercept(invocation);
		}
		else { 
			// 如果拦截器链中不存在拦截器,则调用action实例的处理方法
			// 获取处理的方法名
			String methodName = config.getMethod();
			try {
				Method method = action.getClass().getMethod(methodName);
				result = (String) method.invoke(action);
				// 处理action中关于result的配置
				actionHandler.execute();
				
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("action方法调用失败: " + methodName);
			}
		}
		
		return result;
	}
	
	public ActionContext getInvocationContext() {
		return invocationContext;
	}
}
