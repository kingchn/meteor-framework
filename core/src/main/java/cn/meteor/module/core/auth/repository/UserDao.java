package cn.meteor.module.core.auth.repository;

public interface UserDao {
	
	/**
	 * 根据用户名得到用户id
	 * @param username 用户名
	 * @return 用户id
	 */
	public String findUserIdByUsername(String username);
	
	/**
	 * 根据用户id得到角色id（多个以逗号隔开）
	 * @param userId 用户id
	 * @return 角色id（多个以逗号隔开）
	 */
	public String findRoleIdsByUserId(String userId);
	

//    public User createUser(User user);
//    public void updateUser(User user);
//    public void deleteUser(Long userId);
//
//    public void correlationRoles(Long userId, Long... roleIds);
//    public void uncorrelationRoles(Long userId, Long... roleIds);
//
//    User findOne(Long userId);
//
//    User findByUsername(String username);
//
//    Set<String> findRoles(String username);
//
//    Set<String> findPermissions(String username);
}
