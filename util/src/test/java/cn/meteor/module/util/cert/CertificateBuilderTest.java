package cn.meteor.module.util.cert;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Test;

import cn.meteor.module.util.security.RSAUtils;

public class CertificateBuilderTest {	
	
	/**
	 * root公钥 证书签发者更上一级的key
	 */
	public static  final String ROOT_PUBLIC_KEY_STR="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4O5uY7VhQWUFyF49fHWjAo33nTm4GxjLoLf564I5fbcGtBgO7CouuDyxW17N+SAlSeMFOjQqGTvpcbisryAD3BbrThO1qlxiCzgb5eGYDfGHZ0ZsHlZBkdjsnOImALSRP8vV+CoVYupBUlhwjOlMSlIrni35ZcS/ris1DZ6SNZn6UqaotEucTclLzRSWJOph18Uu3Hxi49GQ72/EouOyrQfWyYojlhaDnGKIeB+8VTMJNjitblf018bAq+PPkly5QYFpmoywZuVk97cu3sIJVxjhWY6RlUuQPBCf3OGKHrRCCOFd5OXCcrdQ1b0ogB6+07ztazzrlBPtp/GNuM00DwIDAQAB";

	/**
	 * root私钥 证书签发者更上一级的key
	 */
	private static final String ROOT_PRIVATE_KEY_STR="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDg7m5jtWFBZQXIXj18daMCjfedObgbGMugt/nrgjl9twa0GA7sKi64PLFbXs35ICVJ4wU6NCoZO+lxuKyvIAPcFutOE7WqXGILOBvl4ZgN8YdnRmweVkGR2Oyc4iYAtJE/y9X4KhVi6kFSWHCM6UxKUiueLfllxL+uKzUNnpI1mfpSpqi0S5xNyUvNFJYk6mHXxS7cfGLj0ZDvb8Si47KtB9bJiiOWFoOcYoh4H7xVMwk2OK1uV/TXxsCr48+SXLlBgWmajLBm5WT3ty7ewglXGOFZjpGVS5A8EJ/c4YoetEII4V3k5cJyt1DVvSiAHr7TvO1rPOuUE+2n8Y24zTQPAgMBAAECggEBALCI8YK4HHiivQMhU8iW2zVqDukLH//EWiztt+yq7ExKlaxLJYsdIEXg/KHlqmrYW+u9jaC8yD290945Pu7DhcP7CPriZTYsA5cilmK5yAlJgyf/EWRN9nBtfGc05vauwUD8zhZTkS1tBY+Hce5mPEYob6taEd9Zp8ceiikCR2Zg1jd2KvyblSG3MsKH1N7zQKxCTTYxLlsfkMc3/patDbLnN8W1euZP/fF1+7yhIygRUAYhQVoGYcIc5jA7sH5/K+sj2vHXV29uMRQldJrgQWQWsNyI0Yy7k1cftmZYVmJD9HBze9wsiND2v97beZLSlB/l0ZtRxcYtBXb0ZGWHqMECgYEA+NBBDSbQKytQqAF7ERu80EFktS98wKsTuZn++gFrs3h2rZe7hwWyMLlkadm9nUmVduFcrnQ6Zb/upu81saVyZ1pwuZVNmoJBL67XAkoFOdzNV2Gu/kMAln4ogtpLMem5VxdR3a2n92ft8dO+/LLLoVVj+UEMJVcwgwls0yJC5xkCgYEA522XRDipCwTThL/wwrKdyxW3DIQ275+T9GfkRSwr03ywpHY0nkwNbE1h9BTpnidJHtOg7kwom+52K6D/k+kiaMkauxnXDwLQcRPOwC3T3OTnPIyKKKU5mGWjZwTIkcTEBGyS7GszLVdvezWyoxnFg18fJxWbxpOYPql1cUvYIWcCgYEAiJd7PscR/MD4tGKrtwatLv7XVIhPSk4gtAGQcpsZDxuQu1fTARrlac49C8cXSyO8Q9kQUk1ISdeGImqr5WxYU9jYjWkjgzECrDSloIIWQF+w8smTkc4iont7reo84ZfXwbHxpWrSDQYnIF0IOh3FoDoKeil1i4JEBYvFDFuAsvECgYBU/m2wk3KZ5mLcfTWp+7lx7X61rRUT+jJ3b3RpZyiNWlzxeawnetzvKphP8SI/gEQdezG+ct/zLKffrBhRNSz6+OjFUF8oVytAbDrhX7NQjAzUsvTGqq+1ZZaWbi5PQsmOqmVnSJOaEmOSB42c0wk0D0o8q3Xzfx9J3CiSvH0a/wKBgGBjy0ZMmSko2jRrIm9B0r0LHtcTVdvCxALM8bszZLouf+7A/BBtt8SVyZGdRfarYbF7Gm7mYN7r/wNIWYmNVpb4QMKblD13XVvcZmHjWml7SOTdZi6Sd3EoqZWoyehppsFCnaymSbWRpnyrg09RoimebVd1tUUHmyLdSnuIF64F";
	
	@Test
	public void testBuildPfx() throws Exception {
		String nsrMc="xxx有限公司(测试)";
		String zsmm="12345678";
		String base64StringPrivateKey="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDSPnWxz8rRqgXLOxHz3mbHZ7s0/VlEDdvisZFoB6z/7569Rbe4W3Z3AMNSBcIcjpVHLmkW/QRKbDFCs/SzOK4q+RxzxmWwYM4VOcfRkSnQ+XKUm3eoEe3AN6ZW4cr6bFZmNjNFZJjZn1HNVTtcdMUyGM9ADo5sT+9MDK1ch8knU0HO0fz7zB2PIgEpqW0g5wW0koTKp/NT6r47MbZwJwKoTu4VWY2PxYiA2PZwNjcKXwzZ+n9ZYg4GpDboHyrM5bxMx84hkz+LbBtcKXU++lkM10D8Qlp3rgkfMO/gtgzYj6Yqdccs0UEhEkaSXh0VJp2eMBzRNd2ZTyGJZS9225X9AgMBAAECggEBAKbQVhPqOhOXIpe426qyOLxYqqoFpnEfyeqZVzTCelprpniou3e0Yk9TtKX82aJM41aiFVHfGStvoro5DJuzCkYgd5nIveBvPrSKruexlQsxEN+mZw9cgla0EO/4oVOG6BqbQJz7j2eESKRxzvKx19DC7JX3trtrxQODP34eYtUmgqhWStDcThZRGHzurfx+QuP+pjbT/ts8GV1CaFqq881CLUgRUYBcuWfQp2FZaHFMLEKSWm3b7PnL+Gxl7GAJSAZmO3sz62pmODime37DBWXEheOL4f/2JOdgDbQbAPBkjb1c97t1jp5lA0ZjX4JP0+rBpNTvnjR9bwQ+Ebqh7KUCgYEA8aWvx/v6vg2gqJBWhqKae4++k7bnajHTL6yCoCggQ4IlUQat6ziACwBEX3FKPE394X6Mt/qS6bczJujsw6CHQNYzSZLoU9Vs1g4PPBLqcnHeHURY2J1eTNIqLPTVwcFMjcbaG5SFSbun/5+6FIn3CJyD+0b1vzWt7yy5LUJ+EwsCgYEA3rtHOT4ICQmDC6jSNseTAg0mUzVYxsZq+N3W/cI3zIEtZZJr4ZXl42jFjMAqy5LFVBuEoxcn0uCkGZSZQlBQmT/9og4HM9p/1NS5HPEC1Dko9wQCvuLXtZQEzTXE+/k6cxRJki3s3rnz5q7dfL8ykHqw51IYNLNRYnuQe7mDoBcCgYEAsfCi7y1y525bhqS6wmuMPD6ORHGkGMPV0j5C9jJ2B6AXYIH9ob86Ml+g+XMQCjYYJGsure97LVT0+sr8RdW4oxrYK25dXBZGDZ3OxBrdvyZwO7bvgZroLMN5wR8NJJ71g1URmMuWFsRzoj15JdSuLw4p9ee74HBjw0J4nYZgFh8CgYEAp+qIx578b9O7+A5ObP4I1oMTYafWRgVQoE6exrClYIgCsZCxxGA98DQMuxAlM1Rzb/VPCaVrYI/7J03gPR5PpTmY/ZOV2oHf3ZSB+k+40kZFm/RqHX7nIP12oT7oQw6iAiBJVutIqKGIP2GVmXaLqGDW/Y3msdR+hsChLKWvlgECgYBoJ5M9c8BhF/sm/YeH8gs+d66WAPs9oZiXJsKH11VmD4G6BQrUgxZqnwmsBbjKWxtCnXLejO2EXpZAEWpsA3N2WO9gxfjM25wuz4TCrQ+l0jA0hidj1DiEA7ENLf0x9Y1X7qttRbQ4mngn8b5/b0giM48wBUMAvXyIpPHQyfbJJA==";
		String base64StringPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0j51sc/K0aoFyzsR895mx2e7NP1ZRA3b4rGRaAes/++evUW3uFt2dwDDUgXCHI6VRy5pFv0ESmwxQrP0sziuKvkcc8ZlsGDOFTnH0ZEp0PlylJt3qBHtwDemVuHK+mxWZjYzRWSY2Z9RzVU7XHTFMhjPQA6ObE/vTAytXIfJJ1NBztH8+8wdjyIBKaltIOcFtJKEyqfzU+q+OzG2cCcCqE7uFVmNj8WIgNj2cDY3Cl8M2fp/WWIOBqQ26B8qzOW8TMfOIZM/i2wbXCl1PvpZDNdA/EJad64JHzDv4LYM2I+mKnXHLNFBIRJGkl4dFSadnjAc0TXdmU8hiWUvdtuV/QIDAQAB";
		
		PublicKey publicKey=RSAUtils.getRSAPublicKeyByBase64StringKey(base64StringPublicKey);
		PrivateKey privateKey=RSAUtils.getRSAPrivateKeyByBase64StringKey(base64StringPrivateKey);

		PublicKey root_publicKey=RSAUtils.getRSAPublicKeyByBase64StringKey(ROOT_PUBLIC_KEY_STR);
		PrivateKey root_privateKey=RSAUtils.getRSAPrivateKeyByBase64StringKey(ROOT_PRIVATE_KEY_STR);

		//证书持有者
		Subject subject = new Subject();
    	subject.setCN(nsrMc);
    	subject.setOU(nsrMc);
    	subject.setO(nsrMc);
    	subject.setL("广州市");
    	subject.setST("广东省");
    	subject.setC("CN");
    	
    	//证书签发者
    	Issuer issuer = new Issuer();
//    	issuer.setCN("广州CA");
//    	issuer.setOU("认证部");
//    	issuer.setO("广州市数字证书管理中心");
//    	issuer.setCN("国家税务总局");
//    	issuer.setOU("广东省税务局");
//    	issuer.setO("国家税务总局数字证书管理中心");
    	issuer.setCN("xxx平台");
    	issuer.setOU("xxx平台");
    	issuer.setO("xxx平台");
    	issuer.setC("CN");

    	String subjectString = subject.getSubjectString();
    	String issuerString = issuer.getIssuerString();
    	
    	Info info = new Info();
    	try {
    		info.setStartDate(DateUtils.parseDate("2018-11-18 08:00:00", "yyyy-MM-dd HH:mm:ss"));
			info.setEndDate(DateUtils.parseDate("2021-11-17 07:59:59", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

//    	BigInteger serialNumber = new BigInteger(128, new Random());//32*4
    	BigInteger serialNumber = new BigInteger(160, new Random());//40*4
//    	BigInteger serialNumber = BigInteger.probablePrime(32, new Random());
//    	BigInteger serialNumber = BigInteger.probablePrime(40, new Random());
    	info.setSerialNumber(serialNumber);
    	
    	
    	CertificateBuilder certificateBuilder = new CertificateBuilder();
    	String caRootPath = "D:/00_test_data/ca";
    	
    	//1.生成server.jks
    	String jksFileName =nsrMc+"tax.jks";
		String jksFilePathname = caRootPath + "/" + jksFileName;
		String password = zsmm;
		KeyStore keyStore = certificateBuilder.generateJKS(jksFilePathname, password);

		KeyPair keyPair_user = new KeyPair(publicKey, privateKey);  //企业key

        String alias = "tax1";
        certificateBuilder.storeJKSWithKey(jksFilePathname, password, alias, subjectString, issuerString, info, keyPair_user);//test_alias, 2018-11-18, PrivateKeyEntry
		
		//2.生成证书请求server.csr
    	String provider = "BC";		
		String csrFileName = nsrMc+"tax.csr";
		String csrFilePathname = caRootPath + "/" + csrFileName;
		String csrContent = certificateBuilder.genCSRFile(csrFilePathname, keyPair_user, subjectString, provider);
    	System.out.println(csrContent);
    	
    	//3.签发单位 server.csr -> server.cer
		String certFilename = nsrMc+"tax.cer";
		String certFilePathname = caRootPath + "/" + certFilename;
		KeyPair keyPair_root = new KeyPair(root_publicKey, root_privateKey);//root key   证书签发者更上一级的key
		PKCS10CertificationRequest pkcs10CertificationRequest = certificateBuilder.convertPemToPKCS10CertificationRequest(csrContent);
		String certContent = certificateBuilder.generateCertFileByCSR(certFilePathname, pkcs10CertificationRequest, subjectString, issuerString, info, keyPair_root);
		System.out.println(certContent);
		
		//4.导入证书链
		
		//5.导入服务器证书
		String aliasForServerCert = "tax2";
		certificateBuilder.importCertToJKS(jksFilePathname, password, aliasForServerCert, certFilePathname);
		
		//6.JKS转换为PFX
		String pfxFileName = nsrMc+"tax.pfx";
		String pfxFilePathname = caRootPath + "/" + pfxFileName;
		certificateBuilder.translateJKSToPFX(pfxFilePathname, jksFilePathname, password);
		
	}
}
