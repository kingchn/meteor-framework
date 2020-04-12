package cn.meteor.module.util.security.sm.test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.meteor.module.util.io.FileUtils;
import cn.meteor.module.util.lang.HexUtils;
import cn.meteor.module.util.security.sm.BCECUtils;
import cn.meteor.module.util.security.sm.SM2Utils;

public class SM2UtilsTest extends GMBaseTest {
	
	static {
        Security.addProvider(new BouncyCastleProvider());//解决：java.security.NoSuchProviderException: no such provider: BC
    }
	
	@Test
    public void testEncryptAndDecrypt_My() {
		try {
//            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
//            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
//            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
//            
//            byte[] priKeyPkcs8Der = BCECUtils.convertECPrivateKeyToPKCS8(priKey, pubKey);
//            System.out.println("private key pkcs8 der:" + ByteUtils.toHexString(priKeyPkcs8Der));
//            String priKeyBase64String = Base64.encodeBase64String(priKeyPkcs8Der);
//            System.out.println("private key base64:" + priKeyBase64String);
//            
//            byte[] pubKeyX509Der = BCECUtils.convertECPublicKeyToX509(pubKey);
//            System.out.println("public key der:" + ByteUtils.toHexString(pubKeyX509Der));
//            String pubKeyBase64String = Base64.encodeBase64String(pubKeyX509Der);
//            System.out.println("public key base64:" + pubKeyBase64String);
            
            
            String priKeyPkcs8DerBase64String = "MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBBIIBVTCCAVECAQEEICVDOxBVbKLjaGuz/3FtzFk0oDi5vJOTdUhyMPTslUrloIHjMIHgAgEBMCwGByqGSM49AQECIQD////+/////////////////////wAAAAD//////////zBEBCD////+/////////////////////wAAAAD//////////AQgKOn6np2fXjRNWp5Lz2UJp/OXifUVq4+S3by9QU2UDpMEQQQyxK4sHxmBGV+ZBEZqOcmUj+MLv/JmC+FxWkWJM0x0x7w3NqL09necWb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA/////v///////////////3ID32shxgUrU7v0CTnVQSMCAQGhRANCAATlTPpt7zxl6JcwZfBL/2XKmFdbJz5hbwiDwJt4uEmWxcDqkj5szi90yqUr7vb34llKs9xFOhUjrgys+EF55eqA";
    		String pubKeyX509DerBase64String = "MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABOVM+m3vPGXolzBl8Ev/ZcqYV1snPmFvCIPAm3i4SZbFwOqSPmzOL3TKpSvu9vfiWUqz3EU6FSOuDKz4QXnl6oA=";
    		
            
//            //私钥
//            byte[] priKeyPkcs8Der2 = Base64.decodeBase64(priKeyPkcs8DerBase64String);
//            BCECPrivateKey ecPriKey = BCECUtils.convertPKCS8ToECPrivateKey(priKeyPkcs8Der2);
//            ECPrivateKeyParameters ecPrivateKeyParameters = BCECUtils.convertPrivateKeyToParameters(ecPriKey);
            
//            //公钥
//            byte[] pubKeyX509Der2 = Base64.decodeBase64(pubKeyX509DerBase64String);
//            BCECPublicKey ecPubKey = BCECUtils.convertX509ToECPublicKey(pubKeyX509Der2);
//            ECPublicKeyParameters ecPublicKeyParameters = BCECUtils.convertPublicKeyToParameters(ecPubKey);
            
            //明文
			String plainTextString = "12345678一二三";
            
//            byte[] encryptedData = SM2Utils.encrypt(ecPublicKeyParameters, plainTextString.getBytes());
			byte[] encryptedData = SM2Utils.encrypt(pubKeyX509DerBase64String, plainTextString.getBytes());//加密
            System.out.println("SM2 encrypt result:\n" + ByteUtils.toHexString(encryptedData));
            String encryptedDataBase64String = Base64.encodeBase64String(encryptedData);
            System.out.println("encryptedData base64:" + encryptedDataBase64String);
            
//            String encryptedDataBase64String= "BGvh5EhyKBOx5DwxXiT32X95bJF22ImtQ+I/JZ1+WP8gdARwqbtNKB7h8beqAOTdrKyU2YiVoSd+" + 
//            		"nDwaXaEfdrXj4C9vCW4ZPYlvekxqHd81xY2VVyKcCx2E/Z0C8YCMq5xPucSFCUN9Z0FXz6H1tLMz";
            
            byte[] encryptedData2 = Base64.decodeBase64(encryptedDataBase64String);
//            byte[] decryptedData = SM2Utils.decrypt(ecPrivateKeyParameters, encryptedData2);
            byte[] decryptedData = SM2Utils.decrypt(priKeyPkcs8DerBase64String, encryptedData2);//解密
            System.out.println("SM2 decrypt result:" + ByteUtils.toHexString(decryptedData));            
            String decryptedDataString = new String(decryptedData);
            System.out.println("decryptedData:" + decryptedDataString);
            
            if (!Arrays.equals(decryptedData, plainTextString.getBytes())) {
                Assert.fail();
            }
            
            
		} catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
	}
	
	@Test
    public void testSignAndVerify_My() {
        try {
    		String privateKey = "MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBBIIBVTCCAVECAQEEIFf8esicQ0k+rhYETe6qb35z20R8XkW3sNRm8Gwo2PRKoIHjMIHgAgEBMCwGByqGSM49AQECIQD////+/////////////////////wAAAAD//////////zBEBCD////+/////////////////////wAAAAD//////////AQgKOn6np2fXjRNWp5Lz2UJp/OXifUVq4+S3by9QU2UDpMEQQQyxK4sHxmBGV+ZBEZqOcmUj+MLv/JmC+FxWkWJM0x0x7w3NqL09necWb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA/////v///////////////3ID32shxgUrU7v0CTnVQSMCAQGhRANCAATUFiNhNm8DahSEtZHCB1YUR2aDk2gDf2JtpJB+4z+dXFb+/oEtXDuG2An2iENJSGOtEBAM/KMSz8vMOHV/s3iX";
    		String publicKey = "MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABNQWI2E2bwNqFIS1kcIHVhRHZoOTaAN/Ym2kkH7jP51cVv7+gS1cO4bYCfaIQ0lIY60QEAz8oxLPy8w4dX+zeJc=";
    		String srcData = "92440101MA59ABCDEF5e224d98-1a7e-40a3-8918-0b9fc4f94c5e";
    		
    		System.out.println("===================== 开始签名");
    		//签名
    		byte[] signedData = SM2Utils.sign(privateKey, srcData.getBytes());
    		String signedDataHexString = ByteUtils.toHexString(signedData);
            System.out.println("SM2 sign result:" + signedDataHexString);
//            String signedDataHexString2 = HexUtils.byteArrayToHex(signedData);
//            System.out.println("SM2 sign result:" + signedDataHexString2.toLowerCase());
//    		String fpSignedData = ByteUtils.toHexString(signedData);
    		
            System.out.println("===================== 开始验签（对上述签名验签）");
    		//验签（对上述签名验签）
    		boolean flag = SM2Utils.verify(publicKey, srcData.getBytes(), HexUtils.hexToByteArray(signedDataHexString));		//注意此处要用decodeBase64
    		System.out.println("SM2 sign verify result（对上述签名验签）: "+ flag);
            
    		
    		System.out.println("===================== 开始验签（对指定值验签）");
    		//验签（对指定值验签）
    		String fpSignedData="MEYCIQCAtNcXKhaNzF64TjgyWY3SNUJp/ZJcoRg/3DA+On70QgIhALU4MI/K38hnk2L9RPwSSyvP0kViHCPojLJGOd/K8MMt";    		
    		System.out.println("fpSignedData:" + fpSignedData);
    		boolean flag2 = SM2Utils.verify(publicKey, srcData.getBytes(), Base64.decodeBase64(fpSignedData));		//注意此处要用decodeBase64
    		System.out.println("SM2 sign verify result（对指定值验签）: "+ flag2);
    		
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSignAndVerify() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            System.out.println("Pri Hex:"
                + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());

            byte[] sign = SM2Utils.sign(priKey, WITH_ID, SRC_DATA);
            System.out.println("SM2 sign with withId result:\n" + ByteUtils.toHexString(sign));
            byte[] rawSign = SM2Utils.decodeDERSM2Sign(sign);
            sign = SM2Utils.encodeSM2SignToDER(rawSign);
            System.out.println("SM2 sign with withId result:\n" + ByteUtils.toHexString(sign));
            boolean flag = SM2Utils.verify(pubKey, WITH_ID, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }

            sign = SM2Utils.sign(priKey, SRC_DATA);
            System.out.println("SM2 sign without withId result:\n" + ByteUtils.toHexString(sign));
            flag = SM2Utils.verify(pubKey, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testEncryptAndDecrypt() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            System.out.println("Pri Hex:"
                + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());

            byte[] encryptedData = SM2Utils.encrypt(pubKey, SRC_DATA);
            System.out.println("SM2 encrypt result:\n" + ByteUtils.toHexString(encryptedData));
            byte[] decryptedData = SM2Utils.decrypt(priKey, encryptedData);
            System.out.println("SM2 decrypt result:\n" + ByteUtils.toHexString(decryptedData));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testKeyPairEncoding() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            byte[] priKeyPkcs8Der = BCECUtils.convertECPrivateKeyToPKCS8(priKey, pubKey);
            System.out.println("private key pkcs8 der length:" + priKeyPkcs8Der.length);
            System.out.println("private key pkcs8 der:" + ByteUtils.toHexString(priKeyPkcs8Der));
            FileUtils.writeFile("D:/ec.pkcs8.pri.der", priKeyPkcs8Der);

            String priKeyPkcs8Pem = BCECUtils.convertECPrivateKeyPKCS8ToPEM(priKeyPkcs8Der);
            FileUtils.writeFile("D:/ec.pkcs8.pri.pem", priKeyPkcs8Pem.getBytes("UTF-8"));
            byte[] priKeyFromPem = BCECUtils.convertECPrivateKeyPEMToPKCS8(priKeyPkcs8Pem);
            if (!Arrays.equals(priKeyFromPem, priKeyPkcs8Der)) {
                throw new Exception("priKeyFromPem != priKeyPkcs8Der");
            }

            BCECPrivateKey newPriKey = BCECUtils.convertPKCS8ToECPrivateKey(priKeyPkcs8Der);

            byte[] priKeyPkcs1Der = BCECUtils.convertECPrivateKeyToSEC1(priKey, pubKey);
            System.out.println("private key pkcs1 der length:" + priKeyPkcs1Der.length);
            System.out.println("private key pkcs1 der:" + ByteUtils.toHexString(priKeyPkcs1Der));
            FileUtils.writeFile("D:/ec.pkcs1.pri", priKeyPkcs1Der);

            byte[] pubKeyX509Der = BCECUtils.convertECPublicKeyToX509(pubKey);
            System.out.println("public key der length:" + pubKeyX509Der.length);
            System.out.println("public key der:" + ByteUtils.toHexString(pubKeyX509Der));
            FileUtils.writeFile("D:/ec.x509.pub.der", pubKeyX509Der);

            String pubKeyX509Pem = BCECUtils.convertECPublicKeyX509ToPEM(pubKeyX509Der);
            FileUtils.writeFile("D:/ec.x509.pub.pem", pubKeyX509Pem.getBytes("UTF-8"));
            byte[] pubKeyFromPem = BCECUtils.convertECPublicKeyPEMToX509(pubKeyX509Pem);
            if (!Arrays.equals(pubKeyFromPem, pubKeyX509Der)) {
                throw new Exception("pubKeyFromPem != pubKeyX509Der");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSM2KeyRecovery() {
        try {
            String priHex = "5DD701828C424B84C5D56770ECF7C4FE882E654CAC53C7CC89A66B1709068B9D";
            String xHex = "FF6712D3A7FC0D1B9E01FF471A87EA87525E47C7775039D19304E554DEFE0913";
            String yHex = "F632025F692776D4C13470ECA36AC85D560E794E1BCCF53D82C015988E0EB956";
            String encodedPubHex = "04FF6712D3A7FC0D1B9E01FF471A87EA87525E47C7775039D19304E554DEFE0913F632025F692776D4C13470ECA36AC85D560E794E1BCCF53D82C015988E0EB956";
            String signHex = "30450220213C6CD6EBD6A4D5C2D0AB38E29D441836D1457A8118D34864C247D727831962022100D9248480342AC8513CCDF0F89A2250DC8F6EB4F2471E144E9A812E0AF497F801";
            byte[] signBytes = ByteUtils.fromHexString(signHex);
            byte[] src = ByteUtils.fromHexString("0102030405060708010203040506070801020304050607080102030405060708");
            byte[] withId = ByteUtils.fromHexString("31323334353637383132333435363738");

            ECPrivateKeyParameters priKey = new ECPrivateKeyParameters(
                new BigInteger(ByteUtils.fromHexString(priHex)), SM2Utils.DOMAIN_PARAMS);
            ECPublicKeyParameters pubKey = BCECUtils.createECPublicKeyParameters(xHex, yHex, SM2Utils.CURVE, SM2Utils.DOMAIN_PARAMS);

            if (!SM2Utils.verify(pubKey, src, signBytes)) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSM2KeyGen2() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            System.out.println("Pri Hex:"
                + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
            System.out.println("Pub X Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
            System.out.println("Pub Point Hex:"
                + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testEncodeSM2CipherToDER() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            byte[] encryptedData = SM2Utils.encrypt(pubKey, SRC_DATA);

            byte[] derCipher = SM2Utils.encodeSM2CipherToDER(encryptedData);
            FileUtils.writeFile("derCipher.dat", derCipher);

            byte[] decryptedData = SM2Utils.decrypt(priKey, SM2Utils.decodeDERSM2Cipher(derCipher));
            if (!Arrays.equals(decryptedData, SRC_DATA)) {
                Assert.fail();
            }

            Assert.assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testGenerateBCECKeyPair() {
        try {
            KeyPair keyPair = SM2Utils.generateKeyPair();
            ECPrivateKeyParameters priKey = BCECUtils.convertPrivateKeyToParameters((BCECPrivateKey) keyPair.getPrivate());
            ECPublicKeyParameters pubKey = BCECUtils.convertPublicKeyToParameters((BCECPublicKey) keyPair.getPublic());

            byte[] sign = SM2Utils.sign(priKey, WITH_ID, SRC_DATA);
            boolean flag = SM2Utils.verify(pubKey, WITH_ID, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }

            sign = SM2Utils.sign(priKey, SRC_DATA);
            flag = SM2Utils.verify(pubKey, SRC_DATA, sign);
            if (!flag) {
                Assert.fail("verify failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
