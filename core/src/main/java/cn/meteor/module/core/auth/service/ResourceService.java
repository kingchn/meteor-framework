package cn.meteor.module.core.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResourceService {
    
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
    void deleteResource(Long resourceId);

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
    void batchUpdateResourceIndex(List<Long> resourceIds);
    
    Object findAllWithExclude(Map<String,Object> excludeMap);
    
    void move(Map<String,Object> source, Map<String,Object> target);
    
    
    
    
    
    /**
     * 根据用户权限得到菜单列表
     * @param permissions 用户权限
     * @return
     */
    List<Map<String, Object>> findMenus(Set<String> permissions);
    
    
}
