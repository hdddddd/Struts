package actionHandler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stack.ValueStack;



/**
 * 
* @ClassName: ActionContext 
* @Description: TODO(通过请求创建actionContent数据中心,用静态方法getContent()获取当前的actionContent) 
* @author (hedy)  
* @date 2017年12月11日 下午3:03:29 
* @version V1.0
 */

public class ActionContext {
	public static final String REQUEST = "request";
	public static final String RESPONSE = "response";
	public static final String SESSION = "session";  //通过request获取
	public static final String APPLICATION = "application";//通过session获取
	public static final String PARAMETERS = "parameters";  //存储请求参数通过request获取
	public static final String VALUESTACK = "valuestack";
	
	private Map<String, Object> context;
	//保证数据的安全，将每个请求的线程的actioncontext对象存放到actionContext的属性中，所以每次get的Context都是自己的那部分数据
	public static ThreadLocal<ActionContext> actionContext = new ThreadLocal<>();
	
	public ActionContext(HttpServletRequest request, HttpServletResponse response, Object action) {
		context = new HashMap<String, Object>();
		// 准备域
		context.put(REQUEST, request);
		context.put(RESPONSE, response);
		context.put(SESSION, request.getSession());
		context.put(APPLICATION, request.getSession().getServletContext());
		context.put(PARAMETERS, request.getParameterMap());
		ValueStack valueStack = new ValueStack();
		valueStack.push(action);
		request.setAttribute(VALUESTACK, valueStack);
		context.put(VALUESTACK, valueStack);
		// 为当前请求线程设置好ActionContext，即将actionContext对象绑定到当前线程上
		actionContext.set(this);
	}
	
	/*
	 * 当前线程对应的ActionContext对象
	 */
	public static ActionContext getContext() {
		return actionContext.get();
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) context.get(REQUEST);
	}
	
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) context.get(RESPONSE);
	}
	
	public HttpSession getSession() {
		return (HttpSession) context.get(SESSION);
	}
	
	public ServletContext getApplication() {
		return (ServletContext) context.get(APPLICATION);
	}
	
	public Map<String, String> getParams() {
		return (Map<String, String>) context.get(PARAMETERS);
	}
	
	public ValueStack getValueStack() {
		return (ValueStack) context.get(VALUESTACK);
	}
}
