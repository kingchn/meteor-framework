package cn.meteor.module.core.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class SimpleAuthenticationInfo implements AuthenticationInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31217118265590034L;
	
	/**
     * The principals identifying the account associated with this AuthenticationInfo instance.
     */
    protected PrincipalCollection principals;
    /**
     * The credentials verifying the account principals.
     */
    protected Object credentials;
	
	public SimpleAuthenticationInfo(Object principal, Object credentials, String realmName) {
		 this.principals = new SimplePrincipalCollection(principal, realmName);
		 this.credentials = credentials;
    }

	@Override
	public PrincipalCollection getPrincipals() {
		return principals;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

}
