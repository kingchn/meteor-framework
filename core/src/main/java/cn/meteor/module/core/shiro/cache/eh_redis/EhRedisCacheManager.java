package cn.meteor.module.core.shiro.cache.eh_redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class EhRedisCacheManager implements CacheManager {
	
    private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
//	private net.sf.ehcache.Cache ehCache;
//	
//	public net.sf.ehcache.Cache getEhCache() {
//        return ehCache;
//    }
//
//    public void setEhCache(net.sf.ehcache.Cache ehCache) {
//        this.ehCache = ehCache;
//    }
    
    private net.sf.ehcache.CacheManager ehcacheManager;

	public net.sf.ehcache.CacheManager getEhcacheManager() {
		return ehcacheManager;
	}

	public void setEhcacheManager(net.sf.ehcache.CacheManager ehcacheManager) {
		this.ehcacheManager = ehcacheManager;
	}

	protected static final long MILLIS_PER_SECOND = 1000;
    protected static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    protected static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT = 30 * MILLIS_PER_MINUTE;
    private long redisCacheTimeout = 4* 60* MILLIS_PER_MINUTE;

    public long getRedisCacheTimeout() {
		return redisCacheTimeout;
	}

	public void setRedisCacheTimeout(long redisCacheTimeout) {
		this.redisCacheTimeout = redisCacheTimeout;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new EhRedisCache<K, V>(name, redisTemplate, ehcacheManager, redisCacheTimeout);
	}
	
}
