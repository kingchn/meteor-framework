package cn.meteor.module.core.shiro.authc;

import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class SimpleSaltedAuthenticationInfo implements SaltedAuthenticationInfo {

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
    
    protected String salt;
	
	public SimpleSaltedAuthenticationInfo(Object principal, Object credentials, String salt, String realmName) {
		 this.principals = new SimplePrincipalCollection(principal, realmName);
		 this.credentials = credentials;
		 this.salt = salt;
    }

	@Override
	public PrincipalCollection getPrincipals() {
		return principals;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public ByteSource getCredentialsSalt() {
		return ByteSource.Util.bytes(this.salt.getBytes());
	}

}
