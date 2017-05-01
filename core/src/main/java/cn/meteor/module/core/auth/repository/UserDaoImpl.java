package cn.meteor.module.core.auth.repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {
	
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
//	@Autowired
//	private AuthSqlCollection authSqlCollection;

	@Override
	public String findUserIdByUsername(String username) {
		String sql = "select id from sys_user where username=? limit 1";
		return getJdbcTemplate().queryForObject(sql, String.class, username);
//		return getJdbcTemplate().queryForObject(authSqlCollection.getQueryUserIdByUsernameSql(), String.class, username);
	}

	@Override
	public String findRoleIdsByUserId(String userId) {
		String sql = "select role_ids from sys_user_app_roles where user_id=? and app_id=3 limit 1";
		return getJdbcTemplate().queryForObject(sql, String.class, userId);
	}

}
