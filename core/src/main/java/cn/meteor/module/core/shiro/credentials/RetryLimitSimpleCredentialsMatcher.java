package cn.meteor.module.core.shiro.credentials;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.ByteSource;

import cn.meteor.module.util.security.MD5Utils;

public class RetryLimitSimpleCredentialsMatcher extends SimpleCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitSimpleCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

//        boolean matches = super.doCredentialsMatch(token, info);
        boolean matches = realDoCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        } else{
        	passwordRetryCache.put(username, retryCount);
        }
        return matches;
    }
    
  public boolean realDoCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
	  boolean isMatch = false;
	  String password = new String((char[])token.getCredentials()); //得到密码
      if (info instanceof SaltedAuthenticationInfo) {
    	  ByteSource byteSource = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
          String salt = new String(byteSource.getBytes());
          password = password + "_" + salt;
      } else {
    	  
      }
	  try {
		String passwordMd5 = MD5Utils.md5Digest(password);
		String passwordStore = new String((char[])info.getCredentials()); //得到存储的密码
		if(passwordStore.equals(passwordMd5)) {
			isMatch = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	  return isMatch;
	}
    

}
