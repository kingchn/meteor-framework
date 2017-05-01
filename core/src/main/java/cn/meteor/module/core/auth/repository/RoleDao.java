package cn.meteor.module.core.auth.repository;

import java.util.List;
import java.util.Map;

public interface RoleDao {
	
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
     * @param resourceId
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
	
	
	/**
	 * 根据角色id得到角色标识
	 * @param roleId 角色id
	 * @return 角色标识
	 */
	String findNameByRoleId(String roleId);
	
	/**
	 * 根据角色id得到资源id（多个以逗号隔开）
	 * @param roleId 角色id
	 * @return 资源id（多个以逗号隔开）
	 */
	String findResourceIdsByRoleId(String roleId);
	

	

//
//    public void correlationPermissions(Long roleId, Long... permissionIds);
//    public void uncorrelationPermissions(Long roleId, Long... permissionIds);

}
