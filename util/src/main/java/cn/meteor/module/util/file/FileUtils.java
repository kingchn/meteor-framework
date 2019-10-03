package cn.meteor.module.util.file;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件工具类
 * @author shenjc
 *
 */
public class FileUtils {
	
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
    
}
