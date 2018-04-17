package interceptor;

import actionHandler.ActionInvocation;



/**
 * 
* @ClassName: Interceptor 
* @Description: TODO(拦截接口) 
* @author (hedy)  
* @version V1.0
 */
public interface Interceptor {
	/*
	 * 执行拦截器初始化工作
	 */
	void init();
	
	/*
	 * 让拦截器做一些释放资源的工作
	 */
	void destory();
	
	/*
	 * 拦截功能,在请求前或请求后执行一些处理
	 */
	String intercept(ActionInvocation invocation);
}
