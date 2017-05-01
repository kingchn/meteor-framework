package cn.meteor.module.core.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceDaoImpl  implements ResourceDao {

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
	public Map<String, Object> createResource(final Map<String, Object> map) {
        final String sql = "insert into sys_resource(name, type, url, icon_skin, permission, parent_id, parent_ids, available) values(?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                int count = 1;
                psst.setString(count++,	map.get("name")!=null				?	"" + map.get("name")									: 	null);
                psst.setString(count++,	map.get("type")!=null				?	"" + map.get("type")										: 	null);
                psst.setString(count++,	map.get("url")!=null					?	"" + map.get("url")										: 	null);
                psst.setString(count++,	map.get("iconSkin")!=null		?	"" + map.get("iconSkin")								: 	null);
                psst.setString(count++,	map.get("permission")!=null		?	"" + map.get("permission")							: 	null);
                psst.setLong(count++,		map.get("parentId")!=null		?	Long.valueOf("" + map.get("parentId")	)		: 	null);
                psst.setString(count++,	map.get("parentIds")!=null		?	"" + map.get("parentIds")								: 	null);
                psst.setBoolean(count++,	map.get("available")!=null&&"1".equals(	map.get("available"))		?	true	: 	false);
                return psst;
            }
        }, keyHolder);
        Long id=keyHolder.getKey().longValue();
        map.put("id", id);
        return map;
	}

    @Override
    public int updateResource(Map<String, Object> map) {
        final String sql = "update sys_resource set name=?, type=?, url=?, icon_skin=?, permission=?, parent_id=?, parent_ids=?, available=? where id=?";
        int rows = jdbcTemplate.update(
                sql,
                map.get("name")!=null				?	"" + map.get("name")									: 	null,
                map.get("type")!=null				?	"" + map.get("type")										: 	null,
                map.get("url")!=null					?	"" + map.get("url")										: 	null,
                map.get("iconSkin")!=null		?	"" + map.get("iconSkin")								: 	null,
                map.get("permission")!=null		?	"" + map.get("permission")							: 	null,
                map.get("parentId")!=null		?	Long.valueOf("" + map.get("parentId")	)		: 	null,
                map.get("parentIds")!=null		?	"" + map.get("parentIds")								: 	null,
                map.get("available")!=null		?	Boolean.valueOf("" + map.get("available"))	: 	null,
                map.get("id")!=null					?	"" + map.get("id")											: 	null);
        return rows;
    }

    public void deleteResource(Long resourceId) {
    	Map<String,Object> resource = findOne(resourceId);
        final String deleteSelfSql = "delete from sys_resource where id=?";
        jdbcTemplate.update(deleteSelfSql, resourceId);
        final String deleteDescendantsSql = "delete from sys_resource where parent_ids like ?";
        String parentIdsWithSelf = "" + resource.get("parent_ids") + resource.get("id") + "/";
        jdbcTemplate.update(deleteDescendantsSql, parentIdsWithSelf + "%");
    }
    
    public Map<String,Object> findOne(Long resourceId) {
        final String sql = "select id, name, type, url, icon_skin as iconSkin, permission, parent_id as parentId, parent_ids as parentIds, available from sys_resource where id=?";
        return  jdbcTemplate.queryForMap(sql, resourceId);
    }
    
    public Map<String,Object> findOneByUrl(String url) {
        final String sql = "select id, name, type, url, icon_skin as iconSkin, permission, parent_id as parentId, parent_ids as parentIds, available from sys_resource where url=?";
        return  jdbcTemplate.queryForMap(sql, url);
    }
    
    @Override
    public List<Map<String, Object>>  findAll() {
    	final String sql = "select id, name, type, url, icon_skin as iconSkin, permission, parent_id as parentId, parent_ids as parentIds, available from sys_resource order by `index` asc,concat(parent_ids, id) asc";
    	return jdbcTemplate.queryForList(sql);
    }    
    
    @Override
    public void batchUpdateResourceIndex(final List<Long> resourceIds) {
        final String sql = "update sys_resource set `index`=? where id=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return resourceIds.size(); //这个方法设定更新记录数，通常List里面存放的都是我们要更新的，所以返回
            }
            public void setValues(PreparedStatement ps, int i)throws SQLException {
            	Long resourceId = resourceIds.get(i);
                ps.setInt(1, i+1);
                ps.setLong(2, resourceId);
            }
        });
    }

	@Override
	public List<Map<String, Object>> findAllWithExclude(Map<String, Object> excludeMap) {
		//TODO 改成not exists 利用索引
        final String sql = "select id, name, parent_id as parentId, parent_ids as parentIds, available from sys_resource where id!=? and parent_ids not like ?";
        String parentIdsWithSelf = "" + excludeMap.get("parentIds") + excludeMap.get("id") + "/";
        return jdbcTemplate.queryForList(sql, excludeMap.get("id"), parentIdsWithSelf + "%");
	}
	
	@Override
	public void move(Map<String,Object> source, Map<String,Object> target) {
		String moveSourceSql = "update sys_resource set parent_id=?,parent_ids=? where id=?";
        jdbcTemplate.update(moveSourceSql, target.get("id"), target.get("parentIds"), source.get("id"));
        String moveSourceDescendantsSql = "update sys_resource set parent_ids=concat(?, substring(parent_ids, length(?))) where parent_ids like ?";
        String sourceParentIdsWithSelf = "" + source.get("parent_ids") + source.get("id") + "/";
        String targetParentIdsWithSelf = "" + target.get("parent_ids") + target.get("id") + "/";
        jdbcTemplate.update(moveSourceDescendantsSql, targetParentIdsWithSelf, sourceParentIdsWithSelf, sourceParentIdsWithSelf + "%");		
	}
	
	
	
	@Override
	public String findPermissionByResourceId(String resourceId) {
		String sql = "select permission from sys_resource where id=?";
		return jdbcTemplate.queryForObject(sql, String.class, resourceId);
	}

}
