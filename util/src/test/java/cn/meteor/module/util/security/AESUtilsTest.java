package cn.meteor.module.util.security;

import org.junit.Test;

public class AESUtilsTest {

	
	@Test
	public void encryptAndDecryptTest() {
        String content = "content123";
        String key = "123456789012345678901234567890ab";
        try {  
            //加密  
            System.out.println("加密前：" + content);  
            String encrypt = AESUtils.encryptString(content, key);  
            System.out.println("加密后：" + encrypt);  
            //解密  
            String decrypt = AESUtils.decryptString(encrypt, key);  
            System.out.println("解密后：" + decrypt);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
}
