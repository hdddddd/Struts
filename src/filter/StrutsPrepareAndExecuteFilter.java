package filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actionHandler.ActionContext;
import actionHandler.ActionInvocation;
import config.ActionConfig;
import config.ConfigurationManager;



/**
 * 
* @ClassName: StrutsPrepareAndExecuteFilter 
* @Description: TODO(核心拦截器) 
* @author (hedy)  
* @version V1.0
 */

public class StrutsPrepareAndExecuteFilter implements Filter {
	/*
	 * 存放拦截器链中每个拦截器全限定类名
	 */
	private List<String> interceptors;
	/*
	 * 请求后缀
	 */
	private String extension;
	/*
	 * 所有action配置信息
	 */
	private Map<String, ActionConfig> actionConfigs;

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 为了调用getServletPath()方法，得到请求的servletPath  把请求强制转换为HttpServletRequest
		HttpServletRequest newRequest = (HttpServletRequest) request;
		HttpServletResponse newResponse = (HttpServletResponse) response;
		// 获取请求路径
		String requestPath = newRequest.getServletPath();
	//	System.out.println(requestPath);
		// 判断是否访问action
		if (!requestPath.endsWith(extension)) {
			// 请求的后缀名不是指定的后缀名,放行
			chain.doFilter(request, response);
			return;
		}
		else {
			// 以指定的后缀名访问,表示需要访问action
			// 获取需要访问action的名称
			requestPath = requestPath.substring(1);
			requestPath = requestPath.replaceAll("." + extension, "");
			// 查找action对应的配置信息
			ActionConfig config = actionConfigs.get(requestPath);
			
			if (config == null) {
				// 未找到该action的配置信息
				throw new RuntimeException("未找到" + requestPath + "的配置信息");
			}
			
			// 创建ActionInvocation,完成对拦截器链和action的调用
			ActionInvocation invocation = new ActionInvocation(interceptors, config, newRequest, newResponse);
			// 获取结果串
			String result = invocation.invoke(invocation);
			// 根据结果串查找到配置信息中的对应路径
			String dispatchPath = config.getResults().get(result);
			if (dispatchPath == null || "".equals(dispatchPath) && !config.getResults().containsKey("stream")) {
				throw new RuntimeException("未找到" + result + "对应的路径");
			}
			else if (config.getResults().containsKey("stream")) {
				return;
			}
			else {
				// 请求转发配置的路径
				newRequest.getRequestDispatcher(dispatchPath).forward(request, response);
				// 释放资源
				ActionContext.actionContext.remove();
			}
			
			return;
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 准备拦截器链配置信息
		interceptors = ConfigurationManager.getInterceptors();
		// 准备constant配置的访问后缀信息
		extension = ConfigurationManager.getConstant("struts.action.extension");
		// action配置信息
		actionConfigs = ConfigurationManager.getActions();
	}

}
