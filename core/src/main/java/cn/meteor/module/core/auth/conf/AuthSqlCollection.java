package cn.meteor.module.core.auth.conf;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthSqlCollection {
	
	@Value("${meteor.jdbc.auth.query.userIdByUsername.sql:}") 
	private String queryUserIdByUsernameSql;
	
	@Value("${meteor.jdbc.auth.query.roleIdsByUserId.sql:}") 
	private String queryRoleIdsByUserIdSql;
	
	@Value("${meteor.jdbc.auth.query.roleByRoleId.sql:}") 
	private String queryRoleByRoleIdSql;
	
	@Value("${meteor.jdbc.auth.query.resourceIdsByRoleId.sql:}") 
	private String queryResourceIdsByRoleId;
	
	@Value("${meteor.jdbc.auth.query.permissionByResourceId.sql:}") 
	private String queryPermissionByResourceId;

	public String getQueryUserIdByUsernameSql() {
		return queryUserIdByUsernameSql;
	}

	public void setQueryUserIdByUsernameSql(String queryUserIdByUsernameSql) {
		this.queryUserIdByUsernameSql = queryUserIdByUsernameSql;
	}

	public String getQueryRoleIdsByUserIdSql() {
		return queryRoleIdsByUserIdSql;
	}

	public void setQueryRoleIdsByUserIdSql(String queryRoleIdsByUserIdSql) {
		this.queryRoleIdsByUserIdSql = queryRoleIdsByUserIdSql;
	}

	public String getQueryRoleByRoleIdSql() {
		return queryRoleByRoleIdSql;
	}

	public void setQueryRoleByRoleIdSql(String queryRoleByRoleIdSql) {
		this.queryRoleByRoleIdSql = queryRoleByRoleIdSql;
	}

	public String getQueryResourceIdsByRoleId() {
		return queryResourceIdsByRoleId;
	}

	public void setQueryResourceIdsByRoleId(String queryResourceIdsByRoleId) {
		this.queryResourceIdsByRoleId = queryResourceIdsByRoleId;
	}

	public String getQueryPermissionByResourceId() {
		return queryPermissionByResourceId;
	}

	public void setQueryPermissionByResourceId(String queryPermissionByResourceId) {
		this.queryPermissionByResourceId = queryPermissionByResourceId;
	}
	
	
	
//    private String queryRolesByUsernameSql;
//    
//    private String queryPermissionsByUsernameSql;
//
//	
//	public String getQueryPermissionsByUsernameSql() {
//		return queryPermissionsByUsernameSql;
//	}
//
//
//	public String getQueryRolesByUsernameSql() {
//		return queryRolesByUsernameSql;
//	}
//
//
//	/**
//     * @param sql The sql to set.
//     */
//    @Autowired
//    public void setQueryRolesByUsernameSql(@Value("${meteor.jdbc.auth.query.rolesByUsername.sql:}") final String queryRolesByUsernameSql) {
//        this.queryRolesByUsernameSql = queryRolesByUsernameSql;
//    }
//    
//
//
//    /**
//     * @param sql The sql to set.
//     */
//    @Autowired
//	public void setQueryPermissionsByUsernameSql(@Value("${meteor.jdbc.auth.query.permissionsByUsername.sql:}") final String queryPermissionsByUsernameSql) {
//		this.queryPermissionsByUsernameSql = queryPermissionsByUsernameSql;
//	}
}
