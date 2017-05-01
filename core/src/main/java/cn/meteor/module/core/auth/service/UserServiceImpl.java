package cn.meteor.module.core.auth.service;


import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.meteor.module.core.auth.repository.ResourceDao;
import cn.meteor.module.core.auth.repository.RoleDao;
import cn.meteor.module.core.auth.repository.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
    private ResourceDao resourceDao;

	@Override
	public Set<String> findRoles(String username) {
		Set<String> roles = new HashSet<String>();
		String userId=userDao.findUserIdByUsername(username);
		if(StringUtils.isNotBlank(userId)) {
			String roleIds = userDao.findRoleIdsByUserId(userId);
			String[] arrayRoleIds = roleIds.split(",");
			
			for (String roleId : arrayRoleIds) {
	            String role = roleDao.findNameByRoleId(roleId);
	            if(role != null) {
	                roles.add(role);
	            }
	        }	       
		}
		 return roles;
	}

	@Override
	public Set<String> findPermissions(String username) {
		Set<String> permissions = new HashSet<String>();
		String userId=userDao.findUserIdByUsername(username);//获取用户id
		if(StringUtils.isNotBlank(userId)) {
			String roleIds = userDao.findRoleIdsByUserId(userId);//获取角色id 多个逗号隔开
			String[] arrayRoleIds = roleIds.split(",");
			for (String roleId : arrayRoleIds) {
	            String resourceIds = roleDao.findResourceIdsByRoleId(roleId);//获取角色对应的资源id 多个逗号隔开
	            String[] arrayResourceIds = resourceIds.split(",");
	            for (String resourceId : arrayResourceIds) {
	            	String permission = resourceDao.findPermissionByResourceId(resourceId);
	            	 permissions.add(permission);
				}
	        }	        
		}
		
		return permissions;
	}
	
	
	
	
	
	

}
