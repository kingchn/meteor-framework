package cn.meteor.module.util.security.sm.cert.test;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.junit.Assert;
import org.junit.Test;

import cn.meteor.module.util.security.sm.BCECUtils;
import cn.meteor.module.util.security.sm.SM2Utils;
import cn.meteor.module.util.security.sm.cert.CommonUtils;
import cn.meteor.module.util.security.sm.cert.SM2CertUtils;
import cn.meteor.module.util.security.sm.cert.SM2PfxMaker;
import cn.meteor.module.util.security.sm.cert.SM2PublicKey;
import cn.meteor.module.util.security.sm.cert.SM2X509CertMaker;
import cn.meteor.module.util.security.sm.test.util.FileUtil;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

public class SM2PfxMakerTest {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String TEST_PFX_PASSWD = "12345678";
    private static final String TEST_PFX_FILENAME = "test.pfx";

    @Test
    public void testMakePfx() {
        try {
            KeyPair subKP = SM2Utils.generateKeyPair();
            X500Name subDN = SM2X509CertMakerTest.buildSubjectDN();
            SM2PublicKey sm2SubPub = new SM2PublicKey(subKP.getPublic().getAlgorithm(),
                (BCECPublicKey) subKP.getPublic());
            byte[] csr = CommonUtils.createCSR(subDN, sm2SubPub, subKP.getPrivate(),
                SM2X509CertMaker.SIGN_ALGO_SM3WITHSM2).getEncoded();
            SM2X509CertMaker certMaker = SM2X509CertMakerTest.buildCertMaker();
            X509Certificate cert = certMaker.makeCertificate(false,
                new KeyUsage(KeyUsage.digitalSignature | KeyUsage.dataEncipherment), csr);

            SM2PfxMaker pfxMaker = new SM2PfxMaker();
            PKCS10CertificationRequest request = new PKCS10CertificationRequest(csr);
            PublicKey subPub = BCECUtils.createPublicKeyFromSubjectPublicKeyInfo(request.getSubjectPublicKeyInfo());
            PKCS12PfxPdu pfx = pfxMaker.makePfx(subKP.getPrivate(), subPub, cert, TEST_PFX_PASSWD);
            byte[] pfxDER = pfx.getEncoded(ASN1Encoding.DER);
            FileUtil.writeFile(TEST_PFX_FILENAME, pfxDER);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPfxSign() {
        //先生成一个pfx
        testMakePfx();

        try {
            byte[] pkcs12 = FileUtil.readFile(TEST_PFX_FILENAME);
            BCECPublicKey publicKey = SM2CertUtils.getPublicKeyFromPfx(pkcs12, TEST_PFX_PASSWD);
            BCECPrivateKey privateKey = SM2CertUtils.getPrivateKeyFromPfx(pkcs12, TEST_PFX_PASSWD);

            String srcData = "1234567890123456789012345678901234567890";
            byte[] sign = SM2Utils.sign(privateKey, srcData.getBytes());
            boolean flag = SM2Utils.verify(publicKey, srcData.getBytes(), sign);
            if (!flag) {
                Assert.fail();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
