package cn.meteor.module.core.shiro.api;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.util.ByteSource;

import cn.meteor.module.util.security.MD5Utils;

public abstract class AbstractShiroImpl implements IShiro {

	private String getEncryptPassword(String password, String salt) {
		try {
			password = password + "_" + salt;
			String passwordMd5 = MD5Utils.md5Digest(password);
			return passwordMd5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		boolean isMatch = false;
		String passwordReq = new String((char[]) token.getCredentials()); // 得到请求的密码
		String salt = null;
		if (info instanceof SaltedAuthenticationInfo) {
			SaltedAuthenticationInfo saltedAuthenticationInfo = (SaltedAuthenticationInfo) info;
			ByteSource byteSource = saltedAuthenticationInfo.getCredentialsSalt();
			salt = new String(byteSource.getBytes());
		} else {

		}
		try {
			String passwordReqEncrypt = getEncryptPassword(passwordReq, salt);
			String passwordStore = new String((char[]) info.getCredentials()); // 得到存储的密码
			if (passwordStore.equals(passwordReqEncrypt)) {
				isMatch = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMatch;
	}
}
