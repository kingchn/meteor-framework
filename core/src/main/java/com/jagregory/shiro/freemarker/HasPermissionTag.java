package com.jagregory.shiro.freemarker;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}</p>
 *
 * @since 0.1
 */
public class HasPermissionTag extends PermissionTag {
    protected boolean showTagBody(String p) {
        boolean isPermitted=isPermitted(p);
    	return isPermitted;
    }
}
