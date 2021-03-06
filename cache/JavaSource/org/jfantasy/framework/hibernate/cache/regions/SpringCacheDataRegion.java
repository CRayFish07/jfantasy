package org.jfantasy.framework.hibernate.cache.regions;

import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.jfantasy.framework.hibernate.cache.util.Timestamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.Region;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.Properties;

public abstract class SpringCacheDataRegion implements Region {

    private static final Log LOG = LogFactory.getLog(SpringCacheDataRegion.class);
    private static final String CACHE_LOCK_TIMEOUT_PROPERTY = "hibernate.cache_lock_timeout";
    private static final int DEFAULT_CACHE_LOCK_TIMEOUT = 60000;

    private final Cache cache;
    private final SpringCacheAccessStrategyFactory accessStrategyFactory;
    private final int cacheLockTimeout;

    SpringCacheDataRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache cache, Properties properties) {
        this.accessStrategyFactory = accessStrategyFactory;
        this.cache = cache;
        final String timeout = properties.getProperty(CACHE_LOCK_TIMEOUT_PROPERTY,Integer.toString(DEFAULT_CACHE_LOCK_TIMEOUT));
        this.cacheLockTimeout = Timestamper.ONE_MS * Integer.decode(timeout);
    }

    protected Cache getCache() {
        return cache;
    }

    public Cache getSpringCache(){
        return getCache();
    }

    protected SpringCacheAccessStrategyFactory getAccessStrategyFactory() {
        return accessStrategyFactory;
    }

    @Override
    public String getName() {
        return getCache().getName();
    }

    @Override
    public void destroy() throws CacheException {
        try {
            getCache().clear();
        } catch (IllegalStateException e) {
            LOG.debug("This can happen if multiple frameworks both try to shutdown ehcache", e);
        }
    }

    @Override
    public long getSizeInMemory() {
        System.err.println("getSizeInMemory 是啥勒！");
        return -1;
    }

    @Override
    public long getElementCountInMemory() {
        System.err.println("getElementCountInMemory 是啥勒！");
        return -1;
    }

    @Override
    public long getElementCountOnDisk() {
        System.err.println("getElementCountOnDisk 是啥勒！");
        return -1;
    }

    @Override
    public Map toMap() {
        System.err.println("toMap 是啥勒！");
        return null;
        /*
        try {
            final Map<Object, Object> result = new HashMap<Object, Object>();
            for (Object key : getCache().getKeys()) {
                result.put(key, getCache().get(key).getObjectValue());
            }
            return result;
        } catch (Exception e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance().handleNonstopCacheException((NonStopCacheException) e);
                return Collections.emptyMap();
            } else {
                throw new CacheException(e);
            }
        }*/
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public int getTimeout() {
        return cacheLockTimeout;
    }

    @Override
    public boolean contains(Object key) {
        System.err.println("contains key！");
        return getCache().get(key) != null;
    }

}