package cn.meteor.module.core.shiro.cache.eh_redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.springframework.data.redis.core.RedisTemplate;

import cn.meteor.module.core.shiro.session.ShiroSession;
import net.sf.ehcache.Element;

public class EhRedisCache<K, V> implements Cache<K, V> {
	
	private static final Logger logger = LogManager.getLogger(EhRedisCache.class);

    /**
     * Backing instance.
     */
    private RedisTemplate<K, V> redisTemplate;
    
    private net.sf.ehcache.Cache ehCache;
    
    /**
     * Backing instance.
     */
    private final Map<K, V> map = new HashMap<>();
    private final Map<K, Long> mapTimestamp = new HashMap<>();

    /**
     * The name of this cache.
     */
    private final String name;
    
    public String getName() {
		return name;
	}


	private String cacheKeyPrefix;
    private static final String SHIRO_CACHE = "shiro-cache:";

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

	public EhRedisCache(String name, RedisTemplate backingRedisTemplate, net.sf.ehcache.CacheManager ehcacheManager, long redisCacheTimeout) {
        if (name == null) {
            throw new IllegalArgumentException("Cache name cannot be null.");
        }
        if (backingRedisTemplate == null) {
            throw new IllegalArgumentException("Backing redisTemplate cannot be null.");
        }
        this.name = name;
        this.cacheKeyPrefix = SHIRO_CACHE + name + ":";
        this.redisTemplate = backingRedisTemplate;
//        this.ehCache = ehCache;
        this.ehCache = ehcacheManager.getCache(name);
        this.redisCacheTimeout = redisCacheTimeout;
    }
    
//    private K getRedisCacheKey(Object k) {
//        return (K) (this.cacheKeyPrefix + k);
//    }
    
	private V getByRedisCacheKey(K redisCacheKey) throws CacheException {//redisCacheKey包括前缀，格式如: shiro-redis-cache:shiro-activeSessionCache:9308fbf1-1f2f-4dc5-826c-9f46b0868317
//		redisTemplate.boundValueOps(redisCacheKey).expire(redisCacheTimeout, TimeUnit.MILLISECONDS);
		return redisTemplate.boundValueOps(redisCacheKey).get();
	}

	private V getByCacheKey(K cacheKey, boolean alsoGetFromRedis, boolean isAlsoGetFromRedisAndUpdateEhcache) {
        	Long cacheTimpstamp = mapTimestamp.get(cacheKey);
//            	System.out.println("cacheTimpstamp:" + new Date(cacheTimpstamp));
        	long cacheExpireTimpstamp=System.currentTimeMillis()-1000;
//            	System.out.println("cacheExpireTimpstamp:" + new Date(cacheExpireTimpstamp));
        	if(cacheTimpstamp!=null  && cacheTimpstamp > cacheExpireTimpstamp) {//如果应用缓存map存在，并且是在1秒前存储的，则从应用缓存map中获取
            	V mapValue = map.get(cacheKey);            	
            	if(mapValue!=null) {
            		return mapValue;
            	}
        	}
        	Element elementValue = ehCache.get(cacheKey);
        	if (elementValue != null) {
//                    if(elementValue.getHitCount() < 5){//缓存命中次数统计小于20次内的使用ehcache的数据
                	V value = (V) elementValue.getObjectValue();
                	logger.info("EhRedisCache L1 (ehcache) get :{}={}  hitCount:{}", cacheKey, value, elementValue.getHitCount());
                	if(value!=null) {
                        map.put(cacheKey, value);
                        mapTimestamp.put(cacheKey, System.currentTimeMillis() );
                	}
                	return value;
//                    }else{
//                    	elementValue.resetAccessStatistics();//统计次数重置为0
//                    }        	
            }
        	
        	if(alsoGetFromRedis==true) {
                logger.debug("EhRedisCache-get-redis-get==========");
            	V value = getByRedisCacheKey(cacheKey);
            	logger.info("EhRedisCache L2 (redis) get :{}={}",cacheKey, value);
            	if(value != null && isAlsoGetFromRedisAndUpdateEhcache == true) {
                	//*==========================================
                	if (value instanceof ShiroSession) {
    	                  ShiroSession shiroSession = (ShiroSession) value;
    	                  shiroSession.setLastAccessTime(new Date());//保证从redis同步到ehcache的最后访问时间得到更新
    	                  shiroSession.setChanged(true);//通知到redis更新
    	                  value = (V) shiroSession;
    	          	}
                	//*==========================================
                	if( !(value instanceof Serializable)) {
                		logger.debug("============>value:" + value);
                	}
                	ehCache.put(new Element(cacheKey, value));//取出来之后缓存到本地
                    map.put(cacheKey, value);
                    mapTimestamp.put(cacheKey, System.currentTimeMillis() );
            	}
            	return value;
        	} else {
        		return null;
        	}        
    	
    }
	
	public V get(K key) throws CacheException {
        logger.debug("Getting object from cache [" + this.getName() + "] for key [" + key + "]");
	        try {
	        if (key == null) {
	            return null;
	        } else {
	        	K cacheKey = (K) (cacheKeyPrefix + key);
	    		return getByCacheKey(cacheKey, true, true);
	        }
        } catch (Throwable t) {
        	logger.error("调用getByCacheKey出错", t);
            throw new CacheException(t);
        }
	}
    
    private void cacheToRedis(K cacheKey, V value) throws CacheException {
//        redisTemplate.boundValueOps(cacheKey).set(value);
        logger.debug("EhRedisCache-put-redis-put==========");
        redisTemplate.boundValueOps(cacheKey).set(value, redisCacheTimeout, TimeUnit.MILLISECONDS);
    }

    public V put(K key, V value) throws CacheException {
        logger.debug("Putting object in cache [" + this.getName() + "] for key [" + key + "]");
        try {
            V previous = get(key);
            if(value!=null) {
                K cacheKey = (K) (cacheKeyPrefix + key);
                map.put(cacheKey, value);
                mapTimestamp.put(cacheKey, System.currentTimeMillis() );
            	if( !(value instanceof Serializable)) {
            		logger.debug("============>value2:" + value);
            	}
            	ehCache.put(new Element(cacheKey, value));            	
            	//*==========================================
        		if (value instanceof Serializable) {
        		    if (value instanceof ValidatingSession) {
        		        if (((ValidatingSession) value).isValid()) {
        		        	if (value instanceof ShiroSession) {//ShiroSession
        		            	ShiroSession shiroSession = (ShiroSession) value;
        		                if (!shiroSession.isChanged()) {//如果session没发生变化
        		                	return previous;
        		                } else {//如果session有发生变化，则更新redis
        		                	shiroSession.setChanged(false);
        		                	shiroSession.setLastAccessTime(new Date());
        		                	value = (V) shiroSession;
        		                	cacheToRedis(cacheKey, value);//缓存到redis
        		                }
        		        	} else {//非ShiroSession
    		                	cacheToRedis(cacheKey, value);//缓存到redis
        		        	}
        		        } else {//无效session
        		        	remove(cacheKey);
        		        }
        		    } else {
                    	cacheToRedis(cacheKey, value);//缓存到redis
        		    }
        			
        		} else {//不能序列化，不处理
        			
        		}
            	//*==========================================      
            }  	
            return previous;
        } catch (Throwable t) {
        	logger.error("调用put出错", t);
            throw new CacheException(t);
        }
    }
    
    private void removeByCacheKey(K cacheKey) {
    	map.remove(cacheKey);
    	ehCache.remove(cacheKey);
        logger.debug("EhRedisCache-remove-redis-remove==========");
        redisTemplate.delete(cacheKey);
    }

    public V remove(K key) throws CacheException {
    	logger.debug("Removing object from cache [" + this.getName() + "] for key [" + key + "]");

        try {
            K cacheKey = (K) (cacheKeyPrefix + key);
//            V previous = get(key);
            V previous = getByCacheKey(cacheKey, false, false);
            removeByCacheKey(cacheKey);
            return previous;
        } catch (Throwable t) {
        	logger.error("调用remove出错", t);
            throw new CacheException(t);
        }
    	
    }

    public void clear() throws CacheException {
    	if (logger.isTraceEnabled()) {
            logger.trace("Clearing all objects from cache [" + this.getName() + "]");
        }
        try {
        	map.clear();
        	ehCache.removeAll();
            logger.debug("EhRedisCache-clear-redis-delete==========");
        	redisTemplate.delete(keys());
        } catch (Throwable t) {
        	logger.error("调用clear出错", t);
            throw new CacheException(t);
        }
		
    }

    public int size() {
    	try {
        	return keys().size();
        } catch (Throwable t) {
        	logger.error("调用size出错", t);
            throw new CacheException(t);
        }
    }

//    public Set<K> keys() {
//    	 try {
//	    	K cacheKey = (K) (cacheKeyPrefix + "*");
//            logger.debug("EhRedisCache-keys-redis-keys==========");
//	        Set<K> keys = redisTemplate.keys(cacheKey);
//	        if (!keys.isEmpty()) {
//	            return Collections.unmodifiableSet(keys);
//	        }
//	        return Collections.emptySet();
//         } catch (Throwable t) {
//             throw new CacheException(t);
//         }
//    }
    
    /* 
     * ehcache keys
     */
    public Set<K> keys() {
        try {
            @SuppressWarnings({"unchecked"})
            List<K> keys = ehCache.getKeys();
            if (!isEmpty(keys)) {
                return Collections.unmodifiableSet(new LinkedHashSet<K>(keys));
            } else {
                return Collections.emptySet();
            }
        } catch (Throwable t) {
        	logger.error("调用keys出错", t);
            throw new CacheException(t);
        }
    }

//    public Collection<V> values() {
//    	try {
////            @SuppressWarnings({"unchecked"})
////            List<K> keys = cache.getKeys();
////            if (!isEmpty(keys)) {
////                List<V> values = new ArrayList<V>(keys.size());
////                for (K key : keys) {
////                    V value = get(key);
////                    if (value != null) {
////                        values.add(value);
////                    }
////                }
////                return Collections.unmodifiableList(values);
////            } else {
////                return Collections.emptyList();
////            }
//            logger.debug("EhRedisCache-values-redis-keys_before==========");
//            Set<K> keys = keys();//已经包括前缀，格式如: shiro-redis-cache:shiro-activeSessionCache:9308fbf1-1f2f-4dc5-826c-9f46b0868317
//            if (!isEmpty(keys)) {
//                List<V> values = new ArrayList<>();
//                for (K k : keys) {
//                    logger.debug("EhRedisCache-values-redis-keys-getByRedisCacheKey_before==========");
//                	V value=getByRedisCacheKey(k);
//                	if(value!=null) {
//                		values.add(value);
//                	}
//                }
//                return Collections.unmodifiableList(values);
//            } else {
//            	return Collections.emptyList();
//            }
//
//        } catch (Throwable t) {
//            throw new CacheException(t);
//        }    	
//    }
    
    /* 
     * ehcache keys
     */
    public Collection<V> values() {
    	try {        	
        	return values_session();
        } catch (Throwable t) {
        	logger.error("调用values出错", t);
            throw new CacheException(t);
        }
//        try {
//            @SuppressWarnings({"unchecked"})
//            List<K> keys = ehCache.getKeys();
//            if (!isEmpty(keys)) {
//                List<V> values = new ArrayList<V>(keys.size());
//                for (K key : keys) {
////                    V value = get(key);
//                	V value = getByCacheKey(key);
//                    if (value != null) {
//                        values.add(value);
//                    }
//                }
//                return Collections.unmodifiableList(values);
//            } else {
//                return Collections.emptyList();
//            }
//        } catch (Throwable t) {
//            throw new CacheException(t);
//        }
    }
    
    public Collection<V> values_session() {
        @SuppressWarnings({"unchecked"})
        List<K> keys = ehCache.getKeys();
        if (!isEmpty(keys)) {
            List<V> values = new ArrayList<V>(keys.size());
            for (K key : keys) {
//                    V value = get(key);
            	V value = getByCacheKey(key, false, false);
                if (value != null && value instanceof Session) {
                    values.add(value);
                }
                if(key!=null && value==null) {//key有 value没有
                	removeByCacheKey(key);//清理
                }
            }
            return Collections.unmodifiableList(values);
        } else {
            return Collections.emptyList();
        }
    }
    
    //////////////////////////
    // From CollectionUtils //
    //////////////////////////
    // CollectionUtils cannot be removed from shiro-core until 2.0 as it has a dependency on PrincipalCollection
    private static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }
    

    public String toString() {
    	return "EhRedisCache [" + this.getName() + "]";
    }
}
