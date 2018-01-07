package cn.meteor.module.core.shiro.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;

import cn.meteor.module.core.shiro.cache.eh_redis.EhRedisCachingSessionDAO;

public class ShiroSessionListener implements SessionListener {

	private static final Logger logger = LogManager.getLogger(ShiroSessionListener.class);

    @Autowired
    private EhRedisCachingSessionDAO sessionDao;

    @Override
    public void onStart(Session session) {
        // 会话创建时触发
        logger.info("ShiroSessionListener session {} 被创建", session.getId());
    }

    @Override
    public void onStop(Session session) {
        sessionDao.delete(session);
        // 会话被停止时触发
        logger.info("ShiroSessionListener session {} 被销毁", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        sessionDao.delete(session);
        //会话过期时触发
        logger.info("ShiroSessionListener session {} 过期", session.getId());
    }

}
