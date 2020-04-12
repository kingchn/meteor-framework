package cn.meteor.module.util.security.sm.test;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.meteor.module.util.security.sm.SM3Utils;

import java.util.Arrays;

public class SM3UtilsTest extends GMBaseTest {
    @Test
    public void testHashAndVerify() {
        try {
            byte[] hash = SM3Utils.hash(SRC_DATA);
            System.out.println("SM3 hash result:\n" + ByteUtils.toHexString(hash));
            boolean flag = SM3Utils.verify(SRC_DATA, hash);
            if (!flag) {
                Assert.fail();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testHmacSM3() {
        try {
            byte[] hmacKey = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
            byte[] hmac = SM3Utils.hmac(hmacKey, SRC_DATA);
            System.out.println("SM3 hash result:\n" + Arrays.toString(hmac));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
