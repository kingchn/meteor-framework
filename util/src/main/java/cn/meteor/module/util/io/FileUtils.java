package cn.meteor.module.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import cn.meteor.module.util.lang.BytesAndInputStreamConvertUtils;

/**
 * 文件工具类
 * @author shenjc
 *
 */
public class FileUtils {
	
	private static FileUtils instance = new FileUtils();
	
	private FileUtils() {
		
	}
	
    /**
     * 将字节数组数据写入到指定路径文件
     * @param filePath 指定文件路径
     * @param data 字节数组数据
     * @throws IOException
     */
    public static void writeFile(String filePath, byte[] data) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filePath, "rw");
            raf.write(data);
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    /**
     * 读取指定路径文件得到字节数组数据
     * @param filePath 指定文件路径
     * @return 字节数组数据
     * @throws IOException
     */
    public static byte[] readFile(String filePath) throws IOException {
        RandomAccessFile raf = null;
        byte[] data;
        try {
            raf = new RandomAccessFile(filePath, "r");
            data = new byte[(int) raf.length()];
            raf.read(data);
            return data;
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }
    
    public static byte[] readFileFromRelativePath(String relativeFilePathName) throws IOException {
//    	InputStream inputStream = ClassLoader.getSystemResourceAsStream(relativeFilePathName);//在tomcat返回是null
    	InputStream inputStream = instance.getClass().getClassLoader().getResourceAsStream(relativeFilePathName);
    	return BytesAndInputStreamConvertUtils.inputStreamToBytes(inputStream);
    }
    
    public static String readFileFromRelativePathToString(String relativeFilePathName) throws IOException {
    	byte[] contentBytes = readFileFromRelativePath(relativeFilePathName);
    	String contentString = new String(contentBytes);
    	return contentString;
    }
    
}
