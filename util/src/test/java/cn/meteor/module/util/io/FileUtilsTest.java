package cn.meteor.module.util.io;

import org.junit.Test;

import cn.meteor.module.util.io.FileUtils;

public class FileUtilsTest {

	@Test
	public void testReadFileFromRelativePath() {
		try {
			String relativeFilePathName = "dozer/dozer-global-configuration.xml";
			byte[] contentBytes = FileUtils.readFileFromRelativePath(relativeFilePathName);
			String contentString = new String(contentBytes);
			System.out.println("contentString:\n"+contentString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}