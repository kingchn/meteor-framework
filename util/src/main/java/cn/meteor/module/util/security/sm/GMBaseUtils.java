package cn.meteor.module.util.security.sm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class GMBaseUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
