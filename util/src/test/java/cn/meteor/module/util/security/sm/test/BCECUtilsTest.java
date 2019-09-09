package cn.meteor.module.util.security.sm.test;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.meteor.module.util.security.sm.BCECUtils;
import cn.meteor.module.util.security.sm.SM2Utils;

public class BCECUtilsTest {

    @Test
    public void testECPrivateKeyPKCS8() {
        try {
            AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPairParameter();
            ECPrivateKeyParameters priKeyParams = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKeyParams = (ECPublicKeyParameters) keyPair.getPublic();
            byte[] pkcs8Bytes = BCECUtils.convertECPrivateKeyToPKCS8(priKeyParams, pubKeyParams);
            BCECPrivateKey priKey = BCECUtils.convertPKCS8ToECPrivateKey(pkcs8Bytes);

            byte[] sign = SM2Utils.sign(priKey, GMBaseTest.WITH_ID, GMBaseTest.SRC_DATA);
            System.out.println("SM2 sign with withId result:\n" + ByteUtils.toHexString(sign));
            boolean flag = SM2Utils.verify(pubKeyParams, GMBaseTest.WITH_ID, GMBaseTest.SRC_DATA, sign);
            if (!flag) {
                Assert.fail("[withId] verify failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
