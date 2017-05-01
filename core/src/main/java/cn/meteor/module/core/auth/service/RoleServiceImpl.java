package cn.meteor.module.core.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.meteor.module.core.auth.repository.RoleDao;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;    
    
	@Override
	public Map<String, Object> createRole(Map<String, Object> map) {
		return roleDao.createRole(map);
	}

	@Override
	public int updateRole(Map<String, Object> map) {
		return roleDao.updateRole(map);
	}

	@Override
	public void deleteRole(Long roleId) {
		roleDao.deleteRole(roleId);
	}
	
	
    
    @Override
    public Map<String,Object> findOne(Long roleId) {
    	Map<String,Object> role = roleDao.findOne(roleId);//得到role实体
    	setResourceIdList(role);
    	return role;
    }
    
    @Override
    public List<Map<String, Object>> findAll() {
    	List<Map<String, Object>> roleList = roleDao.findAll();
    	for (Map<String, Object> role : roleList) {
//    		setResourceIdList(role);
    		setResourceName(role);
		}
    	
    	return roleList;
    }
    
    
    
	/**
	 * 为role设置resourceIdList字段值
	 * @param role
	 */
	private void setResourceIdList(Map<String,Object> role) {
		Object resourceIds = role.get("resourceIds");//取得资源id逗号隔开字符串
    	if(!StringUtils.isEmpty(resourceIds)) {
    		List<Long> resourceIdList = new ArrayList<Long>();//定义资源id列表变量
    		String[] resourceIdArray = ("" +resourceIds).split(",");
	        for(String resourceId : resourceIdArray) {
	            if(StringUtils.isEmpty(resourceId)) {
	                continue;
	            }
	            resourceIdList.add(Long.valueOf(resourceId));
	        }
	    	role.put("resourceIdList", resourceIdList);
    	}
	}
	
	/**
	 * 为role设置resourceNames字段值
	 * @param role
	 */
	private void setResourceName(Map<String,Object> role) {
		Object resourceIds = role.get("resourceIds");//取得资源id逗号隔开字符串
    	if(!StringUtils.isEmpty(resourceIds)) {
    		String resourceNames = AuthFunctions.getResourceNamesByResourceIds(resourceIds);
	    	role.put("resourceNames", resourceNames);
    	}
	}
    




//
//    @Override
//    public Set<String> findRoles(Long... roleIds) {
//        Set<String> roles = new HashSet<String>();
//        for(Long roleId : roleIds) {
//            Role role = findOne(roleId);
//            if(role != null) {
//                roles.add(role.getRole());
//            }
//        }
//        return roles;
//    }
//
//    @Override
//    public Set<String> findPermissions(Long[] roleIds) {
//        Set<Long> resourceIds = new HashSet<Long>();
//        for(Long roleId : roleIds) {
//            Role role = findOne(roleId);
//            if(role != null) {
//                resourceIds.addAll(role.getResourceIds());
//            }
//        }
//        return resourceService.findPermissions(resourceIds);
//    }
}
