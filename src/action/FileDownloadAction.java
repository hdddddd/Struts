package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileDownloadAction {
	private String fileName;	// 代表下载文件的名称

	/*
	 * 获取文件的名称
	 */
	public String getFileName()  {
		return fileName;
	}

	public void setFileName(String fileName){
		// 对文件名称编码
		this.fileName = fileName;
	}

	// 定义了返回InputStream的方法,该方法作为被下载文件的入口
	public InputStream getDownloadFile() throws Exception {
		return getInputStream();
	}
	
	
	public InputStream getInputStream() throws Exception {
		File file = new File("D:\\angular\\workdpace2\\MyStruts\\WebContent\\upload\\helloworld.txt");
		InputStream inputStream = new FileInputStream(file);
		return inputStream;
	}


	public String execute() throws Exception {
		return "success";
	}
}
