package cn.meteor.module.core.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {
	
//	extends JdbcDaoSupport 	
//	@Autowired
//	private DataSource dataSource;
//	@PostConstruct
//	private void initialize() {
//		setDataSource(dataSource);
//	}
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String, Object> createRole(final Map<String, Object> map) {
        final String sql = "insert into sys_role(name, description, resource_ids, available) values(?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                int count = 1;
                psst.setString(count++,	map.get("name")!=null				?	"" + map.get("name")							: 	null);
                psst.setString(count++,	map.get("description")!=null	?	"" + map.get("description")					: 	null);
                psst.setString(count++,	map.get("resourceIds")!=null	?	"" + map.get("resourceIds")					: 	null);
                psst.setBoolean(count++,	map.get("available")!=null&&"1".equals(	map.get("available"))		?	true	: 	false);
                return psst;
            }
        }, keyHolder);
        Long id=keyHolder.getKey().longValue();
        map.put("id", id);
        return map;
	}
	
    @Override
    public int updateRole(Map<String, Object> map) {
    	final String sql = "update sys_role set name=?, description=?, resource_ids=?, available=? where id=?";
        int rows = jdbcTemplate.update(
                sql,
                map.get("name")!=null				?	"" + map.get("name")				: 	null,
                map.get("description")!=null	?	"" + map.get("description")		: 	null,
                map.get("resourceIds")!=null	?	"" + map.get("resourceIds")		: 	null, 
                map.get("available")!=null		?	"" + map.get("available")			: 	null, 
                map.get("id")!=null					?	"" + map.get("id")						: 	null);
        return rows;
    }
    
    @Override
    public void deleteRole(Long roleId) {
    	final String sql = "delete from sys_role where id=?";
        jdbcTemplate.update(sql, roleId);
    }
    
	@Override
    public Map<String,Object> findOne(Long roleId) {
        final String sql = "select id, name, description, resource_ids as resourceIds, available from sys_role where id=?";
        return  jdbcTemplate.queryForMap(sql, roleId);
    }
	
    @Override
    public List<Map<String, Object>>  findAll() {
    	final String sql = "select id, name, description, resource_ids as resourceIds, available from sys_role";
    	return jdbcTemplate.queryForList(sql);
    }
    
    
	
	@Override
	public String findNameByRoleId(String roleId) {
		String sql = "select name from sys_role where id=?";
		return jdbcTemplate.queryForObject(sql, String.class, roleId);
	}
	
	@Override
	public String findResourceIdsByRoleId(String roleId) {
		String sql = "select resource_ids from sys_role where id=?";
		return jdbcTemplate.queryForObject(sql, String.class, roleId);
	}
	

    


}
