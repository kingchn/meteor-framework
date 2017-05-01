package cn.meteor.module.core.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface UserService {

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(String username);

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username);
    
    
    
    
    
    
    
    
    
    

}
