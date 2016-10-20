package shop.trqq.com.bean;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public abstract class Entity implements Serializable {

    /**
     *
     */

    protected String cacheKey;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
