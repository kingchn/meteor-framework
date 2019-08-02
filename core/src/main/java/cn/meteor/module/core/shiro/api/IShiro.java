package cn.meteor.module.core.shiro.api;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import cn.meteor.module.core.shiro.realm.User;

public interface IShiro {

	/**
	 * 根据用户名获取用户实体，realm的doGetAuthenticationInfo需要用到
	 * @param username
	 * @return
	 */
	User getByUsername(String username);
	
	Set<String> getStringPermissionsByUserId(String userId);
		
	boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info);
	
}
