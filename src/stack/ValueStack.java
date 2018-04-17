package stack;

import java.util.Stack;

/**
 * 
* @ClassName: ValueStack 
* @Description: TODO(源码中是一个接口，这里直接当做类使用，用jdk的栈stack实现) 
* @author (hedy)  
* @version V1.0
 */
public class ValueStack {
	private Stack<Object> stack = new Stack<>();
	
	public void push(Object o) {
		stack.push(o);
	}
	
	public Object pop() {
		return stack.pop();
	}
	
	public Object peek() {
		return stack.peek();
	}
}
