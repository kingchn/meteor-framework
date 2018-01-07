package cn.meteor.module.core.shiro.cache.eh_redis;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

public class EhRedisCachingSessionDAO extends CachingSessionDAO {
	
	private static final Logger logger = LogManager.getLogger(EhRedisCachingSessionDAO.class);
    
//    protected static final long MILLIS_PER_SECOND = 1000;
//    protected static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
//    protected static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
//    public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT = 30 * MILLIS_PER_MINUTE;
//    private long globalSessionTimeout = DEFAULT_GLOBAL_SESSION_TIMEOUT;    
//
//    public long getGlobalSessionTimeout() {
//		return globalSessionTimeout;
//	}
//
//	public void setGlobalSessionTimeout(long globalSessionTimeout) {
//		this.globalSessionTimeout = globalSessionTimeout;
//	}

//	private static String cacheKeyPrefix = "shiro-cache-session:";

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//
//	public RedisTemplate<String, Object> getRedisTemplate() {
//		return redisTemplate;
//	}
//
//	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
//		this.redisTemplate = redisTemplate;
//	}
//    
//    private net.sf.ehcache.Cache ehCache;    
//
//	public net.sf.ehcache.Cache getEhCache() {
//        return ehCache;
//    }
//
//    public void setEhCache(net.sf.ehcache.Cache ehCache) {
//        this.ehCache = ehCache;
//    }
    
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        return null; //should never execute because this implementation relies on parent class to access cache, which
        //is where all sessions reside - it is the cache implementation that determines if the
        //cache is memory only or disk-persistent, etc.
    }

    protected void doUpdate(Session session) {
        //does nothing - parent class persists to cache.
    }

    protected void doDelete(Session session) {
        //does nothing - parent class removes from cache.
    }
    
    

//    // 创建session，保存到数据库
//    @Override
//    protected Serializable doCreate(Session session) {
//    	Serializable sessionId = generateSessionId(session);
//        assignSessionId(session, sessionId);
//        String sessionCacheKey = cacheKeyPrefix + sessionId.toString();
//        logger.debug("doCreate session:{}", sessionCacheKey);
//        ehCache.put(new Element(sessionCacheKey, session));
//        logger.debug("EhRedisCachingSessionDAO-doCreate-redis-set==========");
//        redisTemplate.opsForValue().set(sessionCacheKey, session, globalSessionTimeout, TimeUnit.MILLISECONDS);
//        return sessionId;
//    }
//    
//    /*
//     * 重写CachingSessionDAO中readSession方法，如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
//     */
//    public Session readSession(Serializable sessionId) throws UnknownSessionException {
//        Session session = getCachedSession(sessionId);
//        if (session == null) {//            || session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null
//            session = this.doReadSession(sessionId);
//            if (session == null) {
//                throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
//            }
//        }
//        return session;
//    }
//
//    // 获取session
//    @Override
//    protected Session doReadSession(Serializable sessionId) {
//        logger.debug("doReadSession session:{}", sessionId);
//        String sessionCacheKey = cacheKeyPrefix + sessionId.toString();
//        // 先从缓存中获取session，如果没有再去数据库中获取
//        Element element = ehCache.get(sessionCacheKey);
//        if(element!=null) {
//            Object sessionObject = element.getObjectValue();
//        	Session session = (Session) sessionObject;
//        	return session;
//        } else {
//            logger.debug("EhRedisCachingSessionDAO-doReadSession-redis-get==========");
//        	Session session = (Session) redisTemplate.opsForValue().get(sessionCacheKey);
//        	if (session instanceof ShiroSession) {
//                ShiroSession shiroSession = (ShiroSession) session;
//                shiroSession.setLastAccessTime(new Date());//保证从redis同步到ehcache的最后访问时间得到更新
//                ehCache.put(new Element(sessionCacheKey, shiroSession));
//                shiroSession.setChanged(true);//通知到redis更新
//        	} else {
//                ehCache.put(new Element(sessionCacheKey, session));
//        	}
//        	return session;
//        }
//    }
//    
//    public void update(Session session) throws UnknownSessionException {
//        doUpdate(session);
////        if (session instanceof ValidatingSession) {
////            if (((ValidatingSession) session).isValid()) {
////            	//新增自定义session是否变化判断
////            	if (session instanceof ShiroSession) {
////                	ShiroSession ss = (ShiroSession) session;
////                    if (!ss.isChanged()) {
////                        return;
////                    }
////            	}
////                cache(session, session.getId());
////            } else {
////                uncache(session);
////            }
////        } else {
////            cache(session, session.getId());
////        }
//    }
//
//    /* 
//     * 
//     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
//     */
//    @Override
//    protected void doUpdate(Session session) {
//    	//如果会话过期/停止 没必要再更新了
//        try {
//            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
//                return;
//            }
//        } catch (Exception e) {
//            logger.error("ValidatingSession error");
//        }
//        
//        try {
//            if (session instanceof ShiroSession) {
//                // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
//                ShiroSession shiroSession = (ShiroSession) session;
//                if (!shiroSession.isChanged()) {
//                    return;
//                }
////                Transaction tx = null;
////                try {
////                    jedis = jedisUtils.getResource();
////                    // 开启事务
////                    tx = jedis.multi();
////                    ss.setChanged(false);
////                    tx.setex(prefix + session.getId(), seconds, SerializeUtils.serializeToString(ss));
////                    logger.info("sessionId {} name {} 被更新", session.getId(), session.getClass().getName());
////                    // 执行事务
////                    tx.exec();
////                } catch (Exception e) {
////                    if (tx != null) {
////                        // 取消执行事务
////                        tx.discard();
////                    }
////                    throw e;
////                }
//                
//                shiroSession.setChanged(false);
//                shiroSession.setLastAccessTime(new Date());
//                String sessionCacheKey = cacheKeyPrefix + session.getId().toString();
//                logger.debug("doUpdate session:{}", sessionCacheKey);
//                ehCache.put(new Element(sessionCacheKey, shiroSession));
//                logger.debug("EhRedisCachingSessionDAO-doUpdate-redis-hasKey==========");
//                if (!redisTemplate.hasKey(sessionCacheKey)) {
//                    logger.debug("EhRedisCachingSessionDAO-doUpdate-redis-set==========");
//                    redisTemplate.opsForValue().set(sessionCacheKey, shiroSession, globalSessionTimeout, TimeUnit.MILLISECONDS);
//                }
////                redisTemplate.expire(sessionCacheKey, globalSessionTimeout, TimeUnit.MILLISECONDS);
//                
//
//            } else if (session instanceof Serializable) {
////                jedis = jedisUtils.getResource();
////                jedis.setex(prefix + session.getId(), seconds, SerializeUtils.serializeToString((Serializable) session));
//            	
//                String sessionCacheKey = cacheKeyPrefix + session.getId().toString();
//                logger.debug("doUpdate session:{}", sessionCacheKey);
//                ehCache.put(new Element(sessionCacheKey, session));
//                logger.debug("EhRedisCachingSessionDAO-doUpdate-redis-hasKey==========");
//                if (!redisTemplate.hasKey(sessionCacheKey)) {
//                    logger.debug("EhRedisCachingSessionDAO-doUpdate-redis-set==========");
//                    redisTemplate.opsForValue().set(sessionCacheKey, session, globalSessionTimeout, TimeUnit.MILLISECONDS);
//                }
////                redisTemplate.expire(sessionCacheKey, globalSessionTimeout, TimeUnit.MILLISECONDS);
//                
//                logger.info("sessionId {} name {} 作为非ShiroSession对象被更新, ", session.getId(), session.getClass().getName());
//            } else {
//                logger.warn("sessionId {} name {} 不能被序列化 更新失败", session.getId(), session.getClass().getName());
//            }
//        } catch (Exception e) {
//            logger.warn("更新Session失败", e);
//        } finally {
////            jedisUtils.returnResource(jedis);
//        }
//        
////        https://www.cnblogs.com/shihaiming/p/6406640.html
//        	
//
//    }
//
//    /**
//     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
//     */
//    @Override
//    protected void doDelete(Session session) {
//        String sessionCacheKey = cacheKeyPrefix + session.getId().toString();
//        logger.debug("doDelete session:{}", sessionCacheKey);
//        ehCache.remove(sessionCacheKey);
//        logger.debug("EhRedisCachingSessionDAO-doDelete-redis-delete==========");
//        redisTemplate.delete(sessionCacheKey);
//    }
//    
//    public List<byte[]> mget(final byte[]... bytes) {
//        return redisTemplate.execute(new RedisCallback<List<byte[]>>() {
//            @Override
//            public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                return redisConnection.mGet(bytes);
//            }
//        });
//    }
    
//    /**
//     * The Cache instance responsible for caching Sessions.
//     */
//    private Cache<Serializable, Session> activeSessions;
//    
//    private Cache<Serializable, Session> getActiveSessionsCacheLazy() {
//        if (this.activeSessions == null) {
//            this.activeSessions = createActiveSessionsCache();
//        }
//        return activeSessions;
//    }
    
//    /**
//     * 获取当前所有活跃用户，如果用户量多此方法影响性能
//     */
//    @Override
//    public Collection<Session> getActiveSessions() {
//    	Cache<Serializable, Session> cache = getActiveSessionsCacheLazy();
//        if (cache != null) {
//            return cache.values();
//        } else {
//            return Collections.emptySet();
//        }
//    }
    
}
