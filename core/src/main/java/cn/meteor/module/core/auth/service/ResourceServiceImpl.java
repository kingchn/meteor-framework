package cn.meteor.module.core.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.meteor.module.core.auth.repository.ResourceDao;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	@Override
	public Map<String, Object> createResource(Map<String, Object> map) {
		return resourceDao.createResource(map);
	}

	@Override
	public int updateResource(Map<String, Object> map) {
		return resourceDao.updateResource(map);
	}

	@Override
	public void deleteResource(Long resourceId) {
		resourceDao.deleteResource(resourceId);
	}

	@Override
	public Map<String, Object> findOne(Long resourceId) {
		return resourceDao.findOne(resourceId);
	}
	
	@Override
	public Map<String, Object> findOneByUrl(String url) {
		return resourceDao.findOneByUrl(url);
	}

	@Override
	public List<Map<String, Object>> findAll() {
		return resourceDao.findAll();
	}

	@Override
	public void batchUpdateResourceIndex(List<Long> resourceIds) {
		resourceDao.batchUpdateResourceIndex(resourceIds);
	}

	@Override
	public Object findAllWithExclude(Map<String, Object> excludeMap) {
		return resourceDao.findAllWithExclude(excludeMap);
	}

	@Override
	public void move(Map<String, Object> source, Map<String, Object> target) {
		resourceDao.move(source, target);
	}

	@Override
	public List<Map<String, Object>> findMenus(Set<String> permissions) {
		List<Map<String, Object>> allResources = findAll();
		List<Map<String, Object>> menus = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> resource : allResources) {
			Object parentId = resource.get("parentId");
			if (parentId == null || ("" + parentId).equals("0")) {// 如果parentId为空或者如果parentId=0，即顶级节点，则跳过
				continue;
			}
			Object type = resource.get("type");
			if (type == null || !("" + type).equals("menu")) {// 如果type为空或者如果type=menu，则跳过
				continue;
			}
			if (!hasPermission(permissions, resource)) {// 如果没有访问该资源的权限，则跳过
				continue;
			}
			menus.add(resource);
		}
		return menus;
	}

	/**
	 * 判断用户权限是否匹配某资源（判断用户拥有的权限集合是否与某资源的权限匹配）
	 * 
	 * @param permissions
	 *            用户权限
	 * @param resource
	 *            资源对象map
	 * @return 是否有访问该资源的权限
	 */
	private boolean hasPermission(Set<String> permissions, Map<String, Object> resource) {
		Object permissionObject = resource.get("permission");
		if (StringUtils.isEmpty(permissionObject)) {
			return true;
		}
		String permissionString = "" + permissionObject;
		for (String permission : permissions) {
			WildcardPermission p1 = new WildcardPermission(permission);
			WildcardPermission p2 = new WildcardPermission(permissionString);
			if (p1.implies(p2) || p2.implies(p1)) {
				return true;
			}
		}
		return false;
	}

}
