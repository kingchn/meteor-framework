package cn.meteor.module.core.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.meteor.module.core.shiro.authc.SimpleSaltedAuthenticationInfo;

public class UserRealm extends AuthorizingRealm {
	
	private IUserForStore iUserForStore;

    public IUserForStore getiUserForStore() {
		return iUserForStore;
	}

	public void setiUserForStore(IUserForStore iUserForStore) {
		this.iUserForStore = iUserForStore;
	}

	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        authorizationInfo.setRoles(userService.findRoles(username));
//        authorizationInfo.setStringPermissions(userService.findPermissions(username));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String)token.getPrincipal();	//username
        User user = iUserForStore.getByUsername(principal);
//      if(memberUser == null) {
//      throw new UnknownAccountException();//没找到帐号
//  }
//
//  if( Boolean.TRUE.equals(memberUser.getLocked())) {
//      throw new LockedAccountException(); //帐号锁定
//  }
        String credentialsFromStore = (String) user.get("password");
        String saltFromStore = (String) user.get("salt");
        String realmName = getName();
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
//      SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//      		memberUser.getUsername(), //用户名
//      		memberUser.getPassword(), //密码
//              ByteSource.Util.bytes(memberUser.getCredentialsSalt()),//salt=username+salt
//              getName()  //realm name          
//      );
        SimpleSaltedAuthenticationInfo authenticationInfo = new SimpleSaltedAuthenticationInfo(user, credentialsFromStore.toCharArray(), saltFromStore, realmName);
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
