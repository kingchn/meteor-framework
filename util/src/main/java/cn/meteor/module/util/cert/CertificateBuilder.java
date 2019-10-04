package cn.meteor.module.util.cert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;

public class CertificateBuilder {

	   /**
     * 公钥方法
     */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    /**
     * 公私钥公共方法
     */
    /**
     * 根据seed产生密钥对
     * 
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateKeyPair(int seed) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, new SecureRandom(new byte[seed]));	//RSA1024 => 1024、RSA2048 =>2048、SM2 =>256
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }
    

    public KeyStore generateJKS(String jksFilePathname, String password) {
    	KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);
            keyStore.store(new FileOutputStream(jksFilePathname), password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return keyStore;
    }
    
    
    public void storeJKSWithKey(String jksFilePathname, String password, String alias, String subjectString, String issuerString, Info info, KeyPair keyPair_user) {
        KeyStore keyStore;
        try {
            // use exited jks file
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(jksFilePathname), password.toCharArray());
            // generate user's keystore by info[8] -----keypair
          //组装公钥信息
    		SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair_user.getPublic().getEncoded());
        	X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(
        			new X500Name(issuerString), info.getSerialNumber(), info.getStartDate(), info.getEndDate(), new X500Name(subjectString),
    				subjectPublicKeyInfo);
        	
        	//基本限制
        	BasicConstraints basicConstraints = new BasicConstraints(false);
        	x509v3CertificateBuilder.addExtension(Extension.basicConstraints, false, basicConstraints);
        	
        	//CRL分发点
//        	String CRLURI = "http://ica-public.itrus.com.cn/cgi-bin/itruscrl.do?CA=01DAFEE00DDC8D7FB4290EA49AEE5201";
//        	DistributionPointName distributionPoint = new DistributionPointName(new GeneralNames(new GeneralName(GeneralName.uniformResourceIdentifier, CRLURI)));
//        	DistributionPoint[] distPoints = new DistributionPoint[1];
//        	distPoints[0] = new DistributionPoint(distributionPoint, null, null);
//        	x509v3CertificateBuilder.addExtension(Extension.cRLDistributionPoints, false, new CRLDistPoint(distPoints));

        	//密钥用法
        	x509v3CertificateBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.nonRepudiation));//私钥扩展        	
    		
    		ContentSigner contentSigner = createContentSigner(keyPair_user);
    		X509CertificateHolder holder = x509v3CertificateBuilder.build(contentSigner);
    		X509Certificate certificate = null;
    		try {
    			byte[] certBuf = holder.getEncoded();
    			certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(certBuf));
    			System.out.println(certificate);
    			//证书base64编码字符串
//        				System.out.println(Base64.encode(certificate.getEncoded()));
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (CertificateException e) {
    			e.printStackTrace();
    		}            
            
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = certificate;
            keyStore.setKeyEntry(alias, keyPair_user.getPrivate(), password.toCharArray(), chain);//test_alias, 2018-11-18, PrivateKeyEntry
//            keyStore.setCertificateEntry("single_cert", certificate);//single_cert, 2018-11-18, trustedCertEntry,
            keyStore.store(new FileOutputStream(jksFilePathname), password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    private PKCS10CertificationRequest genCSRRequest(KeyPair keyPair, String dirName, String provider) throws NoSuchAlgorithmException, OperatorCreationException {
		String signatureAlgorithm = "SHA1WithRSA";//RSA1024 /  RSA2048 => SHA1WithRSA 、 SM2 => SM3withSM2
		PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(new X500Name(dirName), SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
		JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder(signatureAlgorithm);
		jcaContentSignerBuilder.setProvider(provider);
		ContentSigner contentSigner = jcaContentSignerBuilder.build(keyPair.getPrivate());
		PKCS10CertificationRequest pkcs10CertificationRequest = builder.build(contentSigner);
		return pkcs10CertificationRequest;
    }
    
    private String genCSR(KeyPair keyPair, String dirName, String provider)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, OperatorCreationException, IOException {
		PKCS10CertificationRequest pkcs10CertificationRequest = genCSRRequest(keyPair, dirName, provider);
		PemObject pemObject = new PemObject("NEW CERTIFICATE REQUEST", pkcs10CertificationRequest.getEncoded());
		StringWriter str = new StringWriter();
		JcaPEMWriter pemWriter = new JcaPEMWriter(str);
		pemWriter.writeObject(pemObject);
		pemWriter.close();
		str.close();
		return str.toString();
	}
	
	public String genCSRFile(String csrFilePathname, KeyPair keyPair, String dirName, String provider) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, OperatorCreationException, IOException {
		String content = genCSR(keyPair, dirName, provider);
		FileWriter wr = new java.io.FileWriter(new File(csrFilePathname));
        wr.write(content);
        wr.flush();
        wr.close();
		return content;
	}
	
	
	public PKCS10CertificationRequest convertPemToPKCS10CertificationRequest(String pem) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PKCS10CertificationRequest csr = null;
        ByteArrayInputStream pemStream = null;
        try {
            pemStream = new ByteArrayInputStream(pem.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
//            LOG.error("UnsupportedEncodingException, convertPemToPublicKey", ex);
            ex.printStackTrace();
        }

        Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
        PEMParser pemParser = new PEMParser(pemReader);

        try {
            Object parsedObj = pemParser.readObject();

            System.out.println("PemParser returned: " + parsedObj);

            if (parsedObj instanceof PKCS10CertificationRequest) {
                csr = (PKCS10CertificationRequest) parsedObj;

            }
        } catch (IOException ex) {
//            LOG.error("IOException, convertPemToPublicKey", ex);
        	ex.printStackTrace();
        }

        return csr;
    }
	
	private X509Certificate generateSignedCertificateByCSR(
            PKCS10CertificationRequest csr, String subjectString, String issuerString, Info info, KeyPair keyPair_root) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException,
            CertificateParsingException, CertificateEncodingException,
            SignatureException, OperatorCreationException, IOException {
		
		SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair_root.getPublic().getEncoded());
		
		X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(
    			new X500Name(issuerString), info.getSerialNumber(), info.getStartDate(), info.getEndDate(), new X500Name(subjectString),
				subjectPublicKeyInfo);
    	x509v3CertificateBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.nonRepudiation));
		
		ContentSigner contentSigner = createContentSigner(keyPair_root);
		X509CertificateHolder holder = x509v3CertificateBuilder.build(contentSigner);
		X509Certificate certificate = null;
		try {
			byte[] certBuf = holder.getEncoded();
			certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(certBuf));
//			System.out.println(certificate);
			//证书base64编码字符串
//    				System.out.println(Base64.encode(certificate.getEncoded()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return certificate;
    }
	
	public String generateCertFileByCSR(String certFilePathname, PKCS10CertificationRequest pkcs10CertificationRequest, String subjectString, String issuerString, Info info, KeyPair keyPair_root) throws InvalidKeyException, CertificateParsingException, CertificateEncodingException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, OperatorCreationException, IOException {//createPublicKey
            X509Certificate cert = generateSignedCertificateByCSR(pkcs10CertificationRequest, subjectString, issuerString, info, keyPair_root);
            
            PemObject pemObject = new PemObject("CERTIFICATE", cert.getEncoded());
    		StringWriter stringWriter = new StringWriter();
    		JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
    		pemWriter.writeObject(pemObject);
    		pemWriter.close();
    		stringWriter.close();
    		String content = stringWriter.toString();
    		FileWriter wr = new java.io.FileWriter(new File(certFilePathname));
            wr.write(content);
            wr.flush();
            wr.close();
            return content;
    }
    
	private ContentSigner createContentSigner(KeyPair pair) throws IOException, OperatorCreationException { 
//        AlgorithmIdentifier signatureAlgorithmId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA"); 
        AlgorithmIdentifier signatureAlgorithmId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
        
        AlgorithmIdentifier digestAlgorithmId = new DefaultDigestAlgorithmIdentifierFinder().find(signatureAlgorithmId); 
        AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(pair.getPrivate().getEncoded());
 
        return new BcRSAContentSignerBuilder(signatureAlgorithmId, digestAlgorithmId).build(privateKey); 
    }
    
	public void importCertToJKS(String jksFilePathname, String password, String alias, String certFilePathname)
			throws KeyStoreException, CertificateException, NoSuchAlgorithmException, FileNotFoundException, IOException, InvalidNameException, UnrecoverableKeyException {

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(jksFilePathname), password.toCharArray());

		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) factory.generateCertificate(new FileInputStream(certFilePathname));
		X500Principal principal = certificate.getSubjectX500Principal();
		LdapName ldapDN = new LdapName(principal.getName());
		List<Rdn> rdns = ldapDN.getRdns();
		for (Rdn rdn : rdns) {
			String type = rdn.getType();
			if (type.equals("CN")) {
//				keyStore.setCertificateEntry((String) rdn.getValue(), certificate);
				keyStore.setCertificateEntry(alias, certificate);
//				keyStore.setCertificateEntry("sss", certificate);
//				keyStore.setEntry(alias, entry, protParam);
				
				
//				X509Certificate[] chain = new X509Certificate[1];
//	            chain[0] = certificate;
//	            String alias1 = "test_alias";
//	            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias1, password.toCharArray());
//				keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), chain);//test_alias, 2018-11-18, PrivateKeyEntry
////	            keyStore.setKeyEntry(alias, privateKey.getEncoded(), chain);
				break;
			}
		}
		
//		boolean[] keyUsage = certificate.getKeyUsage();
//		List<String>  ddd= certificate.getExtendedKeyUsage();

		FileOutputStream fos = new FileOutputStream(jksFilePathname);
		keyStore.store(fos, password.toCharArray());
		fos.close();
	}    
	
	public void translateJKSToPFX(String pfxFilePathname, String jksFilePathname, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		KeyStore inputKeyStore = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(jksFilePathname);// jks文件的输入流
		char[] nPassword = null;
		if ((password == null) || password.trim().equals("")) {// 秘钥库密码判断是否为空
			nPassword = null;
		} else {
			nPassword = password.toCharArray();// 秘钥库密码字符数组
		}
		inputKeyStore.load(fis, nPassword);// 从给定的输入流加载此密钥存储区
		fis.close();
		KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");// 指定类型的密钥存储对象
		outputKeyStore.load(null, password.toCharArray());
		Enumeration<String> enums = inputKeyStore.aliases();// 列出此密钥存储库的所有别名
		while (enums.hasMoreElements()) {
			String keyAlias = (String) enums.nextElement();
			System.out.println("alias=>[" + keyAlias + "]");
			if (inputKeyStore.isKeyEntry(keyAlias)) {
				Key key = inputKeyStore.getKey(keyAlias, nPassword);
				java.security.cert.Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
				outputKeyStore.setKeyEntry(keyAlias, key, password.toCharArray(), certChain);
			}
		}
		FileOutputStream out = new FileOutputStream(pfxFilePathname);
		outputKeyStore.store(out, nPassword);

		out.close();
	}

}
