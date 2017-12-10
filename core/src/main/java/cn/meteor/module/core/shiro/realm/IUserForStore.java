package cn.meteor.module.core.shiro.realm;

import java.util.Set;

public interface IUserForStore {

	User getByUsername(String username);
	
	Set<String> getStringPermissionsByUserId(String userId);
	
	
}
