package interceptor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import stack.ValueStack;
import actionHandler.ActionContext;
import actionHandler.ActionInvocation;
import config.ConfigurationManager;



public class FileUploadInterceptor implements Interceptor {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	/**
	 * FileItem:用来封装表单中的元素和数据
	 * ServletFileUpload:处理表单数据,将数据封装到FileItem对象中
	 * DiskFileItemFactory:对象的工厂,可以设定缓冲区的大小和临时存放文件目录
	 * ServletFileUpload处理上传的文件数据,优先保存在缓冲区,如果数据超过了缓冲区大小,则
	 * 保存到硬盘上,存储在DiskFileItemFactory指定目录下的临时文件.数据全部接收完毕后,它
	 * 再从临时文件中将数据写入到上传文件目录下的指定文件中,并删除临时文件.
	 */
	@Override
	public String intercept(ActionInvocation invocation) {
		ActionContext actionContext = invocation.getInvocationContext();
		ValueStack valueStack = actionContext.getValueStack();
		// 获取action对象
		Object action = valueStack.peek();
		// 确定本次请求是否为文件上传请求
		boolean flag=ServletFileUpload.isMultipartContent(actionContext.getRequest());
		if (flag) {
			// 创建文件上传的工厂方法
			FileItemFactory factory = new DiskFileItemFactory();
			// 创建ServletFileUpdaload对象
			ServletFileUpload upload = new ServletFileUpload(factory); 
			// 读取该拦截器对应的参数配置信息
			Map<String, String> paramMap = ConfigurationManager.getInterceptorParam(this.getClass().getName());
			// 获取允许的文件后缀名
			String allowedExtensions = paramMap != null && 
					paramMap.get("fileUpload.allowedExtensions") != null ? paramMap.get("fileUpload.allowedExtensions") : "";
			String[] allowedExtensionArray = allowedExtensions.split(",");
			// 获取允许文件的类型
			String allowedTypes  = paramMap != null && 
					paramMap.get("fileUpload.allowedTypes") != null ? paramMap.get("fileUpload.allowedTypes") : "";
			String[] allowedTypeArray = allowedTypes.split(",");
			// 设置最大允许文件大小
			Long fileMaxSize = paramMap != null && paramMap.get("fileUpload.maximumSize") != null ?
					Long.parseLong(paramMap.get("fileUpload.maximumSize")) : -1L;
			try {
				int count = 0;
				// 使用上传对象从请求对象中解析出提交的所有表单元素
				Map<String,List<FileItem>> listForms = upload.parseParameterMap(actionContext.getRequest());
				Set<Entry<String, List<FileItem>>> set = listForms.entrySet();
				Map<String, Object> map = new HashMap<String, Object>();
				String fileFileName = null;
				String fileContentType = null;
				InputStream inputStream = null;
				Long fileFileSize = 0L;

				for (Entry<String, List<FileItem>> entry : set) {
					fileFileName = entry.getValue().get(0).getName();
					fileContentType = entry.getValue().get(0).getContentType();
					inputStream = entry.getValue().get(0).getInputStream();
					fileFileSize = entry.getValue().get(0).getSize();
					System.out.println(entry.getValue().get(0).getFieldName());
					map.put("fileFileName", fileFileName);
					
					map.put("fileContentType", fileContentType);
					map.put("inputStream", inputStream);
				}
				
				if (allowedExtensionArray.length > 0) {
					for (count = 0; count < allowedExtensionArray.length; count++) {
						if (allowedExtensionArray[count].equals(fileFileName.substring(fileFileName.lastIndexOf(".")))) {
							break;
						}
					}
					
					if (count == allowedExtensionArray.length) {
						throw new RuntimeException("文件后缀名不符合上传要求");
					}
				}
				
				if (allowedTypeArray.length > 0) {
					for (count = 0; count < allowedTypeArray.length; count++) {
						if (allowedTypeArray[count].equals(fileContentType)) {
							break;
						}
					}
					
					if (count == allowedTypeArray.length) {
						throw new RuntimeException("文件类型不符合上传要求");
					}
				}
				
				if (fileFileSize > fileMaxSize) {
					throw new RuntimeException("文件大小不符合上传要求");
				}
				
				BeanUtils.populate(action, map);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			} 
		}
		return invocation.invoke(invocation);
	}
}
