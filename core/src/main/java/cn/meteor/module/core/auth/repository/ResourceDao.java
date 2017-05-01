package cn.meteor.module.core.auth.repository;

import java.util.List;
import java.util.Map;

public interface ResourceDao {	
	
    /**
     * 新增
     * @param map
     * @return 对象map
     */
	Map<String, Object> createResource(Map<String,Object> map);
    
    /**
     * 更新
     * @param map
     * @return 受影响行数
     */
	int updateResource(Map<String,Object> map);
    
    /**
     * 删除
     * @param resourceId
     */
    public void deleteResource(Long resourceId);

    /**
     * 通过id获取一个实体
     * @param resourceId
     * @return 实体对象map
     */
    Map<String,Object> findOne(Long resourceId);
    
    /**
     * 通过url获取一个实体
     * @param url
     * @return 实体对象map
     */
    Map<String,Object> findOneByUrl(String url);
    
    /**
     * 获取所有实体
     * @return 对象实体列表
     */
    List<Map<String, Object>> findAll();
    
    
    
    /**
     * 将接收到的资源id列表按顺序更新其对应的index字段
     * @param resourceIds 资源id列表
     */
    void batchUpdateResourceIndex(final List<Long> resourceIds);
    
    List<Map<String, Object>> findAllWithExclude(Map<String,Object> excludeMap);
    
    void move(Map<String,Object> source, Map<String,Object> target);
    
    
    
	/**
	 * 根据资源id得到权限
	 * @param resourceId 资源id
	 * @return 权限
	 */
	public String findPermissionByResourceId(String resourceId);
    
    
}
