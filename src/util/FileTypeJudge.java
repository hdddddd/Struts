package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileTypeJudge {
	   
    private FileTypeJudge() {}  
      
    /*
     * 将文件头转换成16进制字符串 
     * 返回16进制字符串 
     */  
    private static String bytesToHexString(byte[] src){  
          
        StringBuilder stringBuilder = new StringBuilder();     
        if (src == null || src.length <= 0) {     
            return null;     
        }     
        for (int i = 0; i < src.length; i++) {     
            int v = src[i] & 0xFF;     
            String hv = Integer.toHexString(v);     
            if (hv.length() < 2) {     
                stringBuilder.append(0);     
            }     
            stringBuilder.append(hv);     
        }     
        return stringBuilder.toString();     
    }  
     
    /*
     * 得到文件头 
     * 传入filePath 文件路径 
     */  
    private static String getFileContent(InputStream inputStream) throws IOException {  
          
        byte[] b = new byte[28];     
        try {  
            inputStream.read(b, 0, 28);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw e;  
                }  
            }  
        }  
        return bytesToHexString(b);  
    }  
      
    /*
     * 判断文件类型 
     * 传入filePath 文件路径 
     * 返回文件类型 
     */  
    public static FileType getType(InputStream inputStream) throws IOException {  
    	
        String fileHead = getFileContent(inputStream);  
        if (fileHead == null || fileHead.length() == 0) {  
            return null;  
        }  
        fileHead = fileHead.toUpperCase();  
        FileType[] fileTypes = FileType.values();  
        for (FileType type : fileTypes) {  
            if (fileHead.startsWith(type.getValue())) {  
                return type;  
            }  
        }  
        return null;  
    }  
    
    public static InputStream getInputStream(InputStream inputStream) {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
    	try {
        	byte[] buffer = new byte[1024];  
        	int len;  
        	while ((len = inputStream.read(buffer)) > -1 ) {  
        		byteArrayOutputStream.write(buffer, 0, len);  
        	}  
        	byteArrayOutputStream.flush();  
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    	return input;
    }
}
