package cn.meteor.module.core.auth.service;

import java.util.List;
import java.util.Map;


public interface RoleService {
	
    /**
     * 新增
     * @param map
     * @return 对象map
     */
    Map<String, Object> createRole(Map<String,Object> map);

    /**
     * 更新
     * @param map
     * @return 受影响行数
     */
	int updateRole(Map<String,Object> map);
	
    /**
     * 删除
     * @param roleId
     */
	void deleteRole(Long roleId);
	
    /**
     * 通过id获取一个实体
     * @param roleId
     * @return 实体对象map
     */
	Map<String,Object> findOne(Long roleId);
	
    /**
     * 获取所有实体
     * @return 对象实体列表
     */
	List<Map<String, Object>> findAll();
	
	
	

//    /**
//     * 根据角色编号得到角色标识符列表
//     * @param roleIds
//     * @return
//     */
//    Set<String> findRoles(Long... roleIds);
//
//    /**
//     * 根据角色编号得到权限字符串列表
//     * @param roleIds
//     * @return
//     */
//    Set<String> findPermissions(Long[] roleIds);
}
