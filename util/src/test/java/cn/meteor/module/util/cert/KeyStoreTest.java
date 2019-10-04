package cn.meteor.module.util.cert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class KeyStoreTest {

	public static CertificateChainAndPrivateKey loadKeyStore(byte[] certificateBytes, String password) {
    	CertificateChainAndPrivateKey certificateChainAndPrivateKey = null;
    	InputStream inputStream = null;
		try {
//			KeyStore ks = KeyStore.getInstance("PKCS12");
			KeyStore ks = KeyStore.getInstance("PKCS12","SunJSSE");
			inputStream = new ByteArrayInputStream(certificateBytes);
			char[] passwordChars = null;
			if (StringUtils.isBlank(password)) {
				passwordChars = null;
			} else {
				passwordChars = password.toCharArray();
			}
			ks.load(inputStream, passwordChars);
			String alias = (String) ks.aliases().nextElement();
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, passwordChars);
			Certificate[] certificateChain = ks.getCertificateChain(alias);
			
//			System.out.println(privateKey.toString());
			System.out.println(certificateChain[0]);
			
			certificateChainAndPrivateKey = new CertificateChainAndPrivateKey();
			certificateChainAndPrivateKey.setPrivateKey(privateKey);
			certificateChainAndPrivateKey.setCertificateChain(certificateChain);
		} catch (Exception e) {
			e.printStackTrace();
//			logger.info(e.getMessage());
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return certificateChainAndPrivateKey;
    }
	
	@Test
    public void testLoadKeyStore() {
		try {
			String relativeFilePathname = "cert/test.pfx";
			String filePathname = ClassLoader.getSystemResource(relativeFilePathname).toString();
			filePathname = filePathname.replace("file:/", "");
			File file = new File(filePathname);
			InputStream in= null;
			byte[] certificateBytes= null;
			in = new FileInputStream(file);    //真正要用到的是FileInputStream类的read()方法
			certificateBytes= new byte[in.available()];    //in.available()是得到文件的字节数
			in.read(certificateBytes);    //把文件的字节一个一个地填到bytes数组中
			in.close();
			String password="12345678";
			loadKeyStore(certificateBytes, password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
