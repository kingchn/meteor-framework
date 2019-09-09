package cn.meteor.module.util.security.sm.test;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.meteor.module.util.security.sm.SM4Utils;

public class SM4UtilsTest extends GMBaseTest {
	
	@Test
    public void testEncryptAndDecrypt_My_Ecb_Padding() {
		try {
			//密钥
//			byte[] keyBytesGenerated = SM4Utils.generateKey();
//			String keyBase64String = Base64.encodeBase64String(keyBytesGenerated);
			String keyBase64String = "G5EFvzR0B1vU9vDFCHERTQ==";
			System.out.println("keyBase64String:" + keyBase64String);
			byte[] keyBytes = Base64.decodeBase64(keyBase64String);
			
			//明文
			String plainTextString = "12345678一二三";
			
			//加密
			byte[] cipherTextBytes = SM4Utils.encrypt_Ecb_Padding(keyBytes, plainTextString.getBytes());
			String cipherTextBase64String = Base64.encodeBase64String(cipherTextBytes);
			System.out.println("cipherTextBase64String:" + cipherTextBase64String);
			
			//解密
			byte[] decryptedBytes = SM4Utils.decrypt_Ecb_Padding(keyBytes, Base64.decodeBase64(cipherTextBase64String));
			String decryptedString = new String(decryptedBytes);
			System.out.println("decryptedString:" + decryptedString);
			
		} catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
	}

    @Test
    public void testEncryptAndDecrypt() {
        try {
            byte[] key = SM4Utils.generateKey();
            byte[] iv = SM4Utils.generateKey();
            byte[] cipherText = null;
            byte[] decryptedData = null;

            cipherText = SM4Utils.encrypt_Ecb_NoPadding(key, SRC_DATA_16B);
            System.out.println("SM4 ECB NoPadding encrypt result:\n" + Arrays.toString(cipherText));
            decryptedData = SM4Utils.decrypt_Ecb_NoPadding(key, cipherText);
            System.out.println("SM4 ECB NoPadding decrypt result:\n" + Arrays.toString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA_16B)) {
                Assert.fail();
            }

            cipherText = SM4Utils.encrypt_Ecb_Padding(key, SRC_DATA);
            System.out.println("SM4 ECB Padding encrypt result:\n" + Arrays.toString(cipherText));
            decryptedData = SM4Utils.decrypt_Ecb_Padding(key, cipherText);
            System.out.println("SM4 ECB Padding decrypt result:\n" + Arrays.toString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }

            cipherText = SM4Utils.encrypt_Cbc_Padding(key, iv, SRC_DATA);
            System.out.println("SM4 CBC Padding encrypt result:\n" + Arrays.toString(cipherText));
            decryptedData = SM4Utils.decrypt_Cbc_Padding(key, iv, cipherText);
            System.out.println("SM4 CBC Padding decrypt result:\n" + Arrays.toString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }

            cipherText = SM4Utils.encrypt_Cbc_NoPadding(key, iv, SRC_DATA_16B);
            System.out.println("SM4 CBC NoPadding encrypt result:\n" + Arrays.toString(cipherText));
            decryptedData = SM4Utils.decrypt_Cbc_NoPadding(key, iv, cipherText);
            System.out.println("SM4 CBC NoPadding decrypt result:\n" + Arrays.toString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA_16B)) {
                Assert.fail();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testMac() throws Exception {
        byte[] key = SM4Utils.generateKey();
        byte[] iv = SM4Utils.generateKey();

        byte[] mac = SM4Utils.doCMac(key, SRC_DATA_24B);
        System.out.println("CMAC:\n" + ByteUtils.toHexString(mac).toUpperCase());

        mac = SM4Utils.doGMac(key, iv, 16, SRC_DATA_24B);
        System.out.println("GMAC:\n" + ByteUtils.toHexString(mac).toUpperCase());

        byte[] cipher = SM4Utils.encrypt_Cbc_NoPadding(key, iv, SRC_DATA_32B);
        byte[] cipherLast16 = Arrays.copyOfRange(cipher, cipher.length - 16, cipher.length);
        mac = SM4Utils.doCBCMac(key, iv, null, SRC_DATA_32B);
        if (!Arrays.equals(cipherLast16, mac)) {
            Assert.fail();
        }
        System.out.println("CBCMAC:\n" + ByteUtils.toHexString(mac).toUpperCase());

        cipher = SM4Utils.encrypt_Cbc_Padding(key, iv, SRC_DATA_32B);
        cipherLast16 = Arrays.copyOfRange(cipher, cipher.length - 16, cipher.length);
        mac = SM4Utils.doCBCMac(key, iv, SRC_DATA_32B);
        if (!Arrays.equals(cipherLast16, mac)) {
            Assert.fail();
        }
        System.out.println("CBCMAC:\n" + ByteUtils.toHexString(mac).toUpperCase());
    }
}
