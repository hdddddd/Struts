package action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import config.ConfigurationManager;



public class FileUploadAction {
	private String text;
	private String fileFileName;
	private String fileContentType;
	private InputStream inputStream;
	private static final String UPLOAD_FILE_PATH = "D:\\angular\\workdpace2\\MyStruts\\WebContent\\upload";
	

	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() {
		// 设置目标文件
		File toFile = new  File(UPLOAD_FILE_PATH, this.getFileFileName());
		// 文件输出流
		OutputStream os = null;
		
		try {
			os = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int length = 0;
			// 读取file文件输入到toFile文件中
			while (-1 != (length = inputStream.read(buffer, 0, buffer.length))) {
				os.write(buffer);
			}
		}
		catch (IOException e) {
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
			if (os != null) {
				try {
					os.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
		return "success";
	}
	
	
	public static void main(String[] args) {
		Map<String, String> map = ConfigurationManager.getInterceptorParam("com.hedy.interceptor.FileUploadInterceptor");
		System.out.println(map.toString());
	}
}
