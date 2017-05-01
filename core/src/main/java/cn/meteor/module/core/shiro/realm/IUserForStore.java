package cn.meteor.module.core.shiro.realm;

public interface IUserForStore {

	User getByUsername(String username);
}
