package interceptor;

import org.apache.commons.beanutils.BeanUtils;

import stack.ValueStack;
import actionHandler.ActionContext;
import actionHandler.ActionInvocation;


public class ParametersInterceptor implements Interceptor {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) {
		ActionContext actionContext = invocation.getInvocationContext();
		ValueStack valueStack = actionContext.getValueStack();
		// 获取action对象
		Object action = valueStack.peek();
		try {
			//将请求参数数据填充到action对象里面 
			BeanUtils.populate(action, actionContext.getRequest().getParameterMap());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return invocation.invoke(invocation);
	}

}
