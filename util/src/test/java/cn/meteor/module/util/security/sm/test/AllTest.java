package cn.meteor.module.util.security.sm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.meteor.module.util.security.sm.cert.test.FileSNAllocatorTest;
import cn.meteor.module.util.security.sm.cert.test.SM2CertUtilTest;
import cn.meteor.module.util.security.sm.cert.test.SM2PfxMakerTest;
import cn.meteor.module.util.security.sm.cert.test.SM2PrivateKeyTest;
import cn.meteor.module.util.security.sm.cert.test.SM2X509CertMakerTest;

@RunWith(Suite.class)
@SuiteClasses({BCECUtilsTest.class, SM2UtilsTest.class, SM3UtilsTest.class, SM4UtilsTest.class,
    SM2KeyExchangeUtilsTest.class, SM2PreprocessSignerTest.class,
    // ------------------------------------
    FileSNAllocatorTest.class, SM2CertUtilTest.class, SM2PfxMakerTest.class, SM2PrivateKeyTest.class,
    SM2X509CertMakerTest.class})
public class AllTest {
}
