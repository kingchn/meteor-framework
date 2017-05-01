package cn.meteor.module.core.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


public class AuthFunctions {
	
//  private static OrganizationService organizationService;
//  private static RoleService roleService;
	
	
	private static ResourceService resourceService;

	public static void setResourceService(ResourceService resourceService) {
		AuthFunctions.resourceService = resourceService;
	}
    

	/**
     * obj in collection
     * @param iterable
     * @param element
     * @return
     */
    public static boolean in(Iterable iterable, Object element) {
        if(iterable == null) {
            return false;
        }
        return CollectionUtils.contains(iterable.iterator(), element);
    }

//    /**
//     * 根据id显示组织机构名称
//     * @param organizationId
//     * @return
//     */
//    public static String organizationName(Long organizationId) {
//        Organization organization = getOrganizationService().findOne(organizationId);
//        if(organization == null) {
//            return "";
//        }
//        return organization.getName();
//    }
//
//    /**
//     * 根据id列表显示多个组织机构名称
//     * @param organizationIds
//     * @return
//     */
//    public static String organizationNames(Collection<Long> organizationIds) {
//        if(CollectionUtils.isEmpty(organizationIds)) {
//            return "";
//        }
//
//        StringBuilder s = new StringBuilder();
//        for(Long organizationId : organizationIds) {
//            Organization organization = getOrganizationService().findOne(organizationId);
//            if(organization == null) {
//                return "";
//            }
//            s.append(organization.getName());
//            s.append(",");
//        }
//
//        if(s.length() > 0) {
//            s.deleteCharAt(s.length() - 1);
//        }
//
//        return s.toString();
//    }
//    
//    
//    /**
//     * 根据id显示角色名称
//     * @param roleId
//     * @return
//     */
//    public static String roleName(Long roleId) {
//        Role role = getRoleService().findOne(roleId);
//        if(role == null) {
//            return "";
//        }
//        return role.getDescription();
//    }
//
//    /**
//     * 根据id列表显示多个角色名称
//     * @param roleIds
//     * @return
//     */
//    public static String roleNames(Collection<Long> roleIds) {
//        if(CollectionUtils.isEmpty(roleIds)) {
//            return "";
//        }
//
//        StringBuilder s = new StringBuilder();
//        for(Long roleId : roleIds) {
//            Role role = getRoleService().findOne(roleId);
//            if(role == null) {
//                return "";
//            }
//            s.append(role.getDescription());
//            s.append(",");
//        }
//
//        if(s.length() > 0) {
//            s.deleteCharAt(s.length() - 1);
//        }
//
//        return s.toString();
//    }
    
    /**
     * 根据id显示资源名称
     * @param resourceId
     * @return
     */
    public static String getResourceNameByResourceId(Long resourceId) {
//    	Map<String, Object> resource = getResourceService().findOne(resourceId);
    	Map<String, Object> resource = resourceService.findOne(resourceId);
        if(resource == null) {
            return "";
        }
        return "" + resource.get("name");
    }
    
    /**
     * 根据id(以逗号隔开)显示多个资源名称
     * @param resourceIds
     * @return
     */
    public static String getResourceNamesByResourceIds(Object resourceIds) {
    	if(!StringUtils.isEmpty(resourceIds)) {
    		List<Long> resourceIdList = new ArrayList<Long>();//定义资源id列表变量
    		String[] resourceIdArray = ("" +resourceIds).split(",");
	        for(String resourceId : resourceIdArray) {
	            if(StringUtils.isEmpty(resourceId)) {
	                continue;
	            }
	            resourceIdList.add(Long.valueOf(resourceId));
	        }	        
	        return getResourceNamesByResourceIdList(resourceIdList);	    	
    	} else {
    		return null;
    	}
    	
    }
    
    
    /**
     * 根据id列表显示多个资源名称
     * @param resourceIdList
     * @return
     */
    public static String getResourceNamesByResourceIdList(Collection<Long> resourceIdList) {
        if(CollectionUtils.isEmpty(resourceIdList)) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for(Long resourceId : resourceIdList) {
//        	Map<String, Object> resource = getResourceService().findOne(resourceId);
        	Map<String, Object> resource = resourceService.findOne(resourceId);
            if(resource == null) {
                return "";
            }
            s.append("" + resource.get("name"));
            s.append(",");
        }

        if(s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }



//    public static OrganizationService getOrganizationService() {
//        if(organizationService == null) {
//            organizationService = SpringUtils.getBean(OrganizationService.class);
//        }
//        return organizationService;
//    }
//
//    public static RoleService getRoleService() {
//        if(roleService == null) {
//            roleService = SpringUtils.getBean(RoleService.class);
//        }
//        return roleService;
//    }


}

